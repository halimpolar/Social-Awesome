package cmpe.sjsu.socialawesome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cmpe.sjsu.socialawesome.Utils.FriendUtils;
import cmpe.sjsu.socialawesome.Utils.UserAuth;
import cmpe.sjsu.socialawesome.models.UserSummary;

/**
 * A placeholder fragment containing a simple view.
 */
public class FriendFragment extends SocialFragment {

    public FriendFragment() {
        mTitle = FriendFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        UserSummary mySummary = UserAuth.getInstance().getCurrentUserSummary();
        FriendUtils.addFriend(getActivity(), 0, mySummary);
    }

//    public void followPerson(final UserSummary summaryReceive) {
//        if (summaryReceive.status == 2) {
//            final DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
//            final DatabaseReference currentUserRef = userTableRef.child(UserAuth.getInstance().getCurrentUser().id);
//            final DatabaseReference currentUserFollowRef = currentUserRef.child(FOLOWING_FRIEND_LIST);
//            final String receiveName = summaryReceive.first_name + summaryReceive.last_name;
//
//            currentUserFollowRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.child(summaryReceive.id).exists()) {
//                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//                        alertDialogBuilder.setTitle("Error");
//                        alertDialogBuilder
//                                .setMessage("You already followed " + receiveName + "!")
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();
//                    } else {
//                        currentUserRef.child(FOLOWING_FRIEND_LIST).child(summaryReceive.id).setValue(summaryReceive);
//                        userTableRef.child(summaryReceive.id).child(FOLLOWER_LIST).child(UserAuth.getInstance().getCurrentUser().id).setValue(UserAuth.getInstance().getCurrentUserSummary());
//                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//                        alertDialogBuilder.setTitle("Success");
//                        alertDialogBuilder
//                                .setMessage("You are now following " + receiveName + "!")
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            });
//        } else {
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//            alertDialogBuilder.setTitle("Error");
//            alertDialogBuilder
//                    .setMessage("You can't follow user whose profile is not public!")
//                    .setCancelable(false)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
//        }
//    }
}
