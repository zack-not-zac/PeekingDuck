package peekingduckapp.peekingduck;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFragment extends Fragment {
    private RecyclerView script_recyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private ScriptAdapter scriptAdapter;
    private QueueAdapter queueAdapter;
    private ScriptViewModel scriptVM;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    public void reorderQueue(QueueItem movedItem, int pos_dragged, int pos_target)
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


    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_scripts, container, false);

        MainActivity activity = (MainActivity) getActivity();

        layoutManager = new LinearLayoutManager(getActivity());
        script_recyclerview = v.findViewById(R.id.script_recycler_view);
        script_recyclerview.setLayoutManager(layoutManager);

        if (!activity.isUseQueueAdapter()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Scripts");

            //RecyclerView initialisation
            scriptAdapter = new ScriptAdapter();
            script_recyclerview.setAdapter(scriptAdapter);

            scriptVM = ViewModelProviders.of(this).get(ScriptViewModel.class);

            scriptVM.getAllScripts().observe(getActivity(), new Observer<List<Script>>() {
                @Override
                public void onChanged(@Nullable List<Script> scripts) {
                    scriptAdapter.setScripts(scripts);
                }
            });

            scriptAdapter.setOnItemClickedListener(new ScriptAdapter.onItemClickedListener() {
                @Override
                public void onItemClick(Script script) {
                    //TODO: code for onClick goes here

                    Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            scriptAdapter.setOnDeleteClickedListener(new ScriptAdapter.deleteBtnClickedListener() {
                @Override
                public void onDeleteClick(Script script) {
                    scriptVM.delete(script);
                }
            });
        }
        else if (activity.isUseQueueAdapter()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Script Queue");
            queueAdapter = new QueueAdapter();
            script_recyclerview.setAdapter(queueAdapter);

            //This enables drag & drop to re-order queue items
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



            scriptVM = ViewModelProviders.of(this).get(ScriptViewModel.class);

            scriptVM.viewQueue().observe(getActivity(), new Observer<List<QueueItem>>() {
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
        }
        return v;
    }
}
