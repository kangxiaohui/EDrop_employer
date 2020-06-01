package net.edrop.edrop_employer.fragment;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.activity.ChatViewActivity;
import net.edrop.edrop_employer.activity.FriendAddActivity;
import net.edrop.edrop_employer.activity.FriendListActivity;

import java.util.List;

public class MsgPageFragment extends EaseConversationListFragment {
    private static final String SECTION_STRING = "fragment_string";

    public static MsgPageFragment newInstance(String sectionNumber) {
        MsgPageFragment fragment = new MsgPageFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    //修改悬浮按钮的颜色
    private ColorStateList getColorStateListTest(int colorRes) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };
        int color = ContextCompat.getColor(getContext(), colorRes);
        int[] colors = new int[]{color, color, color, color};
        return new ColorStateList(states, colors);
    }

    private void setListener() {
        fab01Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab01Add.setBackgroundTintList(getColorStateListTest(R.color.color_green));
                fab01Add.setImageResource(isAdd ? R.mipmap.ic_add_white_24dp : R.mipmap.ic_close_white_24dp);
                isAdd = !isAdd;
                rlFriend.setVisibility(isAdd ? View.VISIBLE : View.GONE);
                if (isAdd) {
                    addFriendTranslate1.setTarget(ll[0]);
                    addFriendTranslate1.start();
                    addFriendTranslate2.setTarget(ll[1]);
                    addFriendTranslate2.start();
                }
            }
        });
        //悬浮按钮监听事件
        for (int i = 0; i < fabId.length; i++) {
            fab[i].setBackgroundTintList(getColorStateListTest(R.color.color_green));
            fab[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlFriend.setVisibility(View.GONE);
                    fab01Add.setImageResource(R.mipmap.ic_add_white_24dp);
                    fab01Add.setBackgroundTintList(getColorStateListTest(R.color.color_green));
                    isAdd = false;
                }
            });
        }
        //联系人列表监听事件
        ll[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FriendListActivity.class);
                startActivity(intent);
                rlFriend.setVisibility(View.GONE);
                fab01Add.setImageResource(R.mipmap.ic_add_white_24dp);
                fab01Add.setBackgroundTintList(getColorStateListTest(R.color.color_green));
                isAdd = false;
            }
        });
        //添加联系人监听事件
        ll[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FriendAddActivity.class);
                startActivity(intent);
                rlFriend.setVisibility(View.GONE);
                fab01Add.setImageResource(R.mipmap.ic_add_white_24dp);
                fab01Add.setBackgroundTintList(getColorStateListTest(R.color.color_green));
                isAdd = false;

            }
        });
    }

    @SuppressLint("ResourceType")
    private void initData() {
        addFriendTranslate1 = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.anim.add_friend_anim);
        addFriendTranslate2 = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.anim.add_friend_anim);
    }

    public void initview2() {
        fab01Add =  getView().findViewById(R.id.fab01Add);
        rlFriend =  getView().findViewById(R.id.r_friend);
        for (int i = 0; i < llId.length; i++) {
            ll[i] =  getView().findViewById(llId[i]);
        }
        for (int i = 0; i < fabId.length; i++) {
            fab[i] =  getView().findViewById(fabId[i]);
        }
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        initview2();
        initData();
        setListener();
        fab01Add.setBackgroundTintList(getColorStateListTest(R.color.color_green));
        titleBar.removeAllViews();
    }

    @Override
    protected void initView() {
        super.initView();
        //跳转到会话详情页面
        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                Intent intent = new Intent(getActivity(), ChatViewActivity.class);
                //传递参数
                intent.putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId());
                startActivity(intent);
            }
        });

        //清空集合数据
        conversationList.clear();

        //监听会话消息
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }

    private EMMessageListener emMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            //设置数据
            EaseUI.getInstance().getNotifier().notify(list);
            //刷新页面
            refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
