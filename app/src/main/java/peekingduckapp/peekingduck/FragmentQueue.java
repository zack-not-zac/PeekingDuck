package peekingduckapp.peekingduck;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class FragmentQueue extends Fragment implements Callback {
    private RecyclerView script_recyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private QueueAdapter queueAdapter;
    private ScriptViewModel scriptVM;
    private String hid_path;
    private FloatingActionButton fab;
    private int queue_position = 0;
    private Interpreter interpreter;

    public FragmentQueue() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_scripts, container, false);
        setHasOptionsMenu(true);

        MainActivity activity = (MainActivity) getActivity();

        layoutManager = new LinearLayoutManager(activity);
        script_recyclerview = view.findViewById(R.id.script_recycler_view);
        script_recyclerview.setLayoutManager(layoutManager);

        fab = view.findViewById(R.id.fab);
        fab.hide();
        activity.getSupportActionBar().setTitle("Script Queue");

        queueAdapter = new QueueAdapter();
        script_recyclerview.setAdapter(queueAdapter);

        scriptVM = ViewModelProviders.of(this).get(ScriptViewModel.class);

        scriptVM.viewQueue().observe(activity, new Observer<List<QueueItem>>() {
            @Override
            public void onChanged(@Nullable List<QueueItem> queue) {
                queueAdapter.setQueue(queue);
            }
        });

        queueAdapter.setOnItemClickedListener(new QueueAdapter.onItemClickedListener() {
            @Override
            public void onItemClick(QueueItem item) {
                Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        queueAdapter.setOnDeleteClickedListener(new QueueAdapter.deleteBtnClickedListener() {
            @Override
            public void onDeleteClick(QueueItem item) {
                scriptVM.removeFromQueue(item);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {
                int pos_dragged = dragged.getAdapterPosition();
                int pos_target = target.getAdapterPosition();

                QueueItem movedItem = queueAdapter.getQueueItem(dragged.getAdapterPosition());
                movedItem.setPos(pos_target);
                scriptVM.editQueueItem(movedItem);

                Toast.makeText(getContext(),"New Queue Position: " + movedItem.getPos() + "\nOld Queue Position: " + pos_dragged, Toast.LENGTH_SHORT).show();

                queueAdapter.notifyItemMoved(pos_dragged,pos_target);

                reorderQueue(movedItem, pos_dragged, pos_target);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            }
        });
        helper.attachToRecyclerView(script_recyclerview);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_queue, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.queue_run:
                run_queue();
                return true;
            case R.id.queue_delete:
                clear_queue();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reorderQueue(QueueItem movedItem, int pos_dragged, int pos_target)
    {
        for (int x = 0; x < queueAdapter.getItemCount(); x++) {
            //increments the queue value of each item by 1 when an item is dragged above others
            if (queueAdapter.getQueueItem(x).getPos() == movedItem.getPos() && queueAdapter.getQueueItem(x).getID() != movedItem.getID())
            {
                QueueItem item = queueAdapter.getQueueItem(x);
                if (pos_dragged < pos_target) {
                    item.setPos(item.getPos() - 1);

                }
                else
                {
                    item.setPos(item.getPos() + 1);
                }
                scriptVM.editQueueItem(item);
            }
            /*if (queueAdapter.getQueueItem(x).getPos() > movedItem.getPos())
            {
                QueueItem item = queueAdapter.getQueueItem(x);
                item.setPos(item.getPos() + 1);
                scriptVM.editQueueItem(item);
            }*/
        }
    }

    private void unbundle_file() {
        InputStream is = getResources().openRawResource(getResources().getIdentifier("hid_gadget_test", "raw", getActivity().getPackageName()));
        boolean successful_copy = false;

        File file = new File(getActivity().getFilesDir(), "hid_gadget_test");
        hid_path = file.getAbsolutePath();

        // If file is not already unpacked
        if(file.exists()) file.delete();
        try {
            OutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            successful_copy = true;
            output.flush();
            output.close();
            is.close();
        } catch (Exception e) {
            successful_copy = false;
        }

        if (successful_copy) {
            try {
                Process proc = Runtime.getRuntime()
                        .exec(new String[]{"su", "-c", "chmod 755 " + hid_path});
                proc.waitFor();
            } catch (Exception e) {
            }
        }
    }

    private void run_queue() {
        unbundle_file();
        queue_position = 0;
        if(queueAdapter.getItemCount() > 0) {
            QueueItem item = queueAdapter.getQueueItem(queue_position);
            String script = item.getScript_body();
            interpreter = new Interpreter(hid_path, this);
            interpreter.run(script, 0);
        } else {
            Toast.makeText(getActivity(), "No items in queue to run", Toast.LENGTH_LONG).show();
        }
    }

    private void clear_queue() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm")
                .setMessage("Are you sure you wish to clear the queue?")
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Clearing Queue", Toast.LENGTH_LONG).show();
                        scriptVM.removeAllFromQueue();
                    }
                })
                .setNegativeButton("No", null).show();
    }

    @Override
    public void run_next_queue_item() {
        queue_position++;
        Log.d("SCRIPT", "Running next queue item " + queue_position + " / " + queueAdapter.getItemCount());
        if(queue_position < queueAdapter.getItemCount()) {
            QueueItem item = queueAdapter.getQueueItem(queue_position);
            String script = item.getScript_body();
            interpreter.run(script, 1500);
        }
    }
}