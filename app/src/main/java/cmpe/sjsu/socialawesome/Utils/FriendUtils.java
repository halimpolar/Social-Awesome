package cmpe.sjsu.socialawesome.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import cmpe.sjsu.socialawesome.models.User;
import cmpe.sjsu.socialawesome.models.UserSummary;

import static cmpe.sjsu.socialawesome.StartActivity.USERS_TABLE;
import static cmpe.sjsu.socialawesome.models.User.FOLLOWER_LIST;
import static cmpe.sjsu.socialawesome.models.User.FOLOWING_FRIEND_LIST;
import static cmpe.sjsu.socialawesome.models.User.FRIEND_LIST;
import static cmpe.sjsu.socialawesome.models.User.PENDING_FRIEND_LIST;
import static cmpe.sjsu.socialawesome.models.User.WAITING_FRIEND_LIST;


/**
 * Created by bing on 5/11/17.
 */

public class FriendUtils {
    private static UserSummary mSummary = new UserSummary();

    //add friend by email
//        FriendUtils.addFriendByEmail(getActivity(),"lam.tran@sjsu.edu");
//        FriendUtils.addFriendByEmail(getActivity(),"sheilashi0112@gmail.com");
    public static void addFriendByEmail(final Context context, final String email) {

        final DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
        Query query = userTableRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    new Thread(new Runnable() {
                        public void run() {
                            emailFriendRequest(context, email, UserAuth.getCurrentUserSummary());
                        }
                    }).start();
                    UserSummary newUserSummary = new UserSummary();
                    newUserSummary.email = email;
                    userTableRef.child(UserAuth.getInstance().getCurrentUser().id).child(WAITING_FRIEND_LIST).push().setValue(newUserSummary);
                } else {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        User user = postSnapshot.getValue(User.class);
                        getUserSummary(user);
                        addFriend(context, 0, mSummary);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void getUserSummary(User user) {
        mSummary.id = user.id;
        mSummary.email = user.email;
        mSummary.first_name = user.first_name;
        mSummary.last_name = user.last_name;
        mSummary.profilePhotoURL = user.profilePhotoURL;
        mSummary.status = user.status;
    }

    private static void emailFriendRequest(final Context context, final String email, final UserSummary userSummary) {
        Mail m = new Mail("bingtest0112@gmail.com", "01120112");
        String[] toArr = {email};
        m.setTo(toArr);
        m.setFrom("bingtest0112@gmail.com");
        m.setSubject("Invitation from " + userSummary.first_name + " " + userSummary.last_name);
        m.setBody("Your friend " + userSummary.first_name + " " + userSummary.last_name + " is inviting you to join our app, SocialAwesome!");
        try {
            if (m.send()) {
//                Toast.makeText(context, "Email was sent successfully.", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(context, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
//            Toast.makeText(context, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            Log.e("MailApp", "Could not send email", e);
        }
    }

    //type: 0-friend, 1-follow
    public static void addFriend(final Context context, final int type, final String id) {
        final DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
        Query query = userTableRef.orderByChild("id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder
                            .setMessage("Id did not exist!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        User user = postSnapshot.getValue(User.class);
                        getUserSummary(user);
                        addFriend(context, type, mSummary);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    //add friend and follow on profile
//        UserSummary mySummary = UserAuth.getInstance().getCurrentUserSummary();
//        FriendUtils.addFriend(getActivity(), 1, mySummary);
    //type: 0-friend, 1-follow
    public static void addFriend(final Context context, int type, final UserSummary summaryReceive) {
        String nodeSent = null;
        String nodeReceive = null;
        String dialogSuccess = null;
        String dialogDuplicate = null;
        String dialogPrivate = null;
        switch (type) {
            case 0:
                nodeSent = WAITING_FRIEND_LIST;
                nodeReceive = PENDING_FRIEND_LIST;
                dialogDuplicate = "You already sent friend request to ";
                dialogSuccess = "Friend request sent to ";
                dialogPrivate = "You can't add user whose profile is not public as friend!";
                break;
            case 1:
                nodeSent = FOLOWING_FRIEND_LIST;
                nodeReceive = FOLLOWER_LIST;
                dialogDuplicate = "You already followed ";
                dialogSuccess = "You are now following ";
                dialogPrivate = "You can't follow user whose profile is not public!";
                break;
            default:
        }
        if (summaryReceive.id.equals(UserAuth.getInstance().getCurrentUser().id)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Error");
            alertDialogBuilder
                    .setMessage("You can't follow or be friend with yourself!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            if (summaryReceive.status == 2) {
                final DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
                final DatabaseReference currentUserRef = userTableRef.child(UserAuth.getInstance().getCurrentUser().id);
                final DatabaseReference currentUserFollowRef = currentUserRef.child(nodeSent);
                final DatabaseReference followerRef = userTableRef.child(summaryReceive.id).child(nodeReceive);
                final String receiveName = summaryReceive.first_name + summaryReceive.last_name;
                final String dialogDuFinal = dialogDuplicate;
                final String dialogSuFinal = dialogSuccess;

                currentUserFollowRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(summaryReceive.id).exists()) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setTitle("Error");
                            alertDialogBuilder
                                    .setMessage(dialogDuFinal + receiveName + "!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        } else {
                            currentUserFollowRef.child(summaryReceive.id).setValue(summaryReceive);
                            followerRef.child(UserAuth.getInstance().getCurrentUser().id).setValue(UserAuth.getInstance().getCurrentUserSummary());
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setTitle("Success");
                            alertDialogBuilder
                                    .setMessage(dialogSuFinal + receiveName + "!")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder
                        .setMessage(dialogPrivate)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }

    //accept friend request
//    FriendUtils.unFollowFriend(getActivity(), 0, "NFJKWuqd15MaVWYZYsm0lD9ve5J3");
    //type: 0-friend, 1-follow
    public static void unFollowFriend(final Context context, final int type, final String id) {
        final DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
        Query query = userTableRef.orderByChild("id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder
                            .setMessage("Id did not exist!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        User user = postSnapshot.getValue(User.class);
                        getUserSummary(user);
                        unFollowFriend(context, type, mSummary);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //type: 0-friend, 1-follow
    public static void unFollowFriend(final Context context, int type, final UserSummary summaryReceive) {
        String nodeSent = null;
        String nodeReceive = null;
        String dialogSuccess = null;
        String dialogError = null;
        switch (type) {
            case 0:
                nodeSent = PENDING_FRIEND_LIST;
                nodeReceive = WAITING_FRIEND_LIST;
                dialogError = "You never received friend request from ";
                dialogSuccess = "You are now friend with ";
                break;
            case 1:
                nodeSent = FOLOWING_FRIEND_LIST;
                nodeReceive = FOLLOWER_LIST;
                dialogError = "You never followed ";
                dialogSuccess = "You are now unfollowing ";
                break;
            default:
        }

        final DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
        final DatabaseReference currentUserRef = userTableRef.child(UserAuth.getInstance().getCurrentUser().id);
        //my following, my pending
        final DatabaseReference currentUserFollowRef = currentUserRef.child(nodeSent);
        //other follower, other waiting
        final DatabaseReference followerRef = userTableRef.child(summaryReceive.id).child(nodeReceive);
        final String receiveName = summaryReceive.first_name + summaryReceive.last_name;
        final String dialogSuFinal = dialogSuccess;
        final String dialogErrorFinal = dialogError;
        final int functionType = type;

        currentUserFollowRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(summaryReceive.id).exists()) {
                    currentUserFollowRef.child(summaryReceive.id).removeValue();
                    followerRef.child(UserAuth.getInstance().getCurrentUser().id).removeValue();
                    if (functionType == 0) {
                        currentUserRef.child(FRIEND_LIST).child(summaryReceive.id).setValue(summaryReceive);
                        userTableRef.child(summaryReceive.id).child(FRIEND_LIST).child(UserAuth.getInstance().getCurrentUser().id).setValue(UserAuth.getInstance().getCurrentUserSummary());
                    }
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Success");
                    alertDialogBuilder
                            .setMessage(dialogSuFinal + receiveName + "!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder
                            .setMessage(dialogErrorFinal + receiveName + "!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

//    public static UserSummary getSummaryById(final Context context, String id) {
//        final DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
//        Query query = userTableRef.orderByChild("id").equalTo(id);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() == null) {
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                    alertDialogBuilder.setTitle("Error");
//                    alertDialogBuilder
//                            .setMessage("Id did not exist!")
//                            .setCancelable(false)
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
//                } else {
//                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                        User user = postSnapshot.getValue(User.class);
//                        getUserSummary(user);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//        return mSummary;
//    }

//    public static UserSummary getSummaryByEmail(final Context context, String email) {
//        final DatabaseReference userTableRef = FirebaseDatabase.getInstance().getReference().child(USERS_TABLE);
//        Query query = userTableRef.orderByChild("email").equalTo(email);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() == null) {
//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                    alertDialogBuilder.setTitle("Error");
//                    alertDialogBuilder
//                            .setMessage("Email did not exist!")
//                            .setCancelable(false)
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
//                } else {
//                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                        User user = postSnapshot.getValue(User.class);
//                        getUserSummary(user);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//        return mSummary;
//    }
}
