package ch.epfl.sweng.melody.util;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.memory.MemoryAdapter;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.user.UserContactInfo;

public class FetchingUtils {

    public static void fetchMemoriesFromDatabase(final List<Memory> memList, final MemoryAdapter memAdapter, final long memoryStartTime, final User user) {
        DatabaseHandler.getAllMemoriesWithSingleListener(new ValueEventListener() {  //Listener is only used on fetching
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(user == null) {
                    for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                        Memory memory = memDataSnapshot.getValue(Memory.class);
                        assert memory != null;
                        if (memory.getLongId() > memoryStartTime) {
                            if (memory.getPrivacy() == Memory.Privacy.PUBLIC || (memory.getPrivacy() == Memory.Privacy.SHARED && isFriendsMemory(memory.getUser().getId()))) {
                                memList.add(memory);
                                //memoryAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } else {
                    for (DataSnapshot memDataSnapshot : dataSnapshot.getChildren()) {
                        Memory memory = memDataSnapshot.getValue(Memory.class);
                        assert memory != null;
                        if (memory.getUser().equals(user) && memory.getLongId() > memoryStartTime) {
                                memList.add(memory);
                                //memoryAdapter.notifyDataSetChanged();
                        }
                    }
                }


                    /*if (memory.getLongId() > memoryStartTime) {
                        if (isNewMemory(memory.getId(), memoryList)) {
                            if(memory.getPrivacy() == Memory.Privacy.PUBLIC){
                                memoryList.add(memory);
                                memoryAdapter.notifyItemInserted(memoryList.size() - 1); //was memoryAdapter.notifyDataSetChanged();
                            }

                            else if(memory.getPrivacy() == Memory.Privacy.SHARED && isFriendsMemory(memory.getUser().getId())){
                                memoryList.add(memory);
                                memoryAdapter.notifyDataSetChanged();
                            }
                        }
                    }*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void createMemoriesListener(final List<Memory> memList, final MemoryAdapter memAdapter, final long memoryStartTime, final User user) {
        DatabaseHandler.setCustomListenerToMemories(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Memory memory = dataSnapshot.getValue(Memory.class);
                assert memory != null;

                if (user == null) {
                    if (memory.getLongId() > memoryStartTime) {
                        if (memory.getPrivacy() == Memory.Privacy.PUBLIC || (memory.getPrivacy() == Memory.Privacy.SHARED && isFriendsMemory(memory.getUser().getId()))) {
                            memList.add(memory);
                            memAdapter.notifyItemInserted(memList.size() - 1); //Toast.makeText(getApplicationContext(), "New memories have been uploaded", Toast.LENGTH_LONG).show();
                            //does not work because recyclerview keeps adding new memories at the bottom
                        }
                    }
                } else {
                    if (memory.getUser().equals(user) && memory.getLongId() > memoryStartTime) {
                        memList.add(memory);
                        memAdapter.notifyItemInserted(memList.size() - 1);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Memory memory = dataSnapshot.getValue(Memory.class);
                assert memory != null;
                int position = memList.indexOf(memory);
                if (position != -1) {
                    memList.set(position, memory);
                    memAdapter.notifyItemChanged(position);
                };
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Memory memory = dataSnapshot.getValue(Memory.class);
                assert memory != null;
                int position = memList.indexOf(memory);
                if (position != -1) {
                    memList.remove(position);
                    memAdapter.notifyItemRemoved(position);
                };
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private static boolean isFriendsMemory(String memoryAuthorId) {
        Map<String, UserContactInfo> Friends = MainActivity.getUser().getFriends();
        for (UserContactInfo friend : Friends.values()) {
            String friendUserId = friend.getUserId();
            if(friendUserId.equals(memoryAuthorId))
                return true;
        }
        return false;
    }

    private static boolean isNewMemory(String memoryId, List<Memory> memoryList) {
        for (Memory m : memoryList) {
            if (memoryId.equals(m.getId()))
                return false;
        }
        return true;
    }

}
