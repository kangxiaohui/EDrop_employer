package net.edrop.edrop_employer.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.edrop.edrop_employer.R;

/**
 * Created by mysterious
 * User: mysterious
 * Date: 2019/11/25
 * Time: 16:42
 */
public class CommunityPageFragment extends Fragment {
    private static final String SECTION_STRING = "fragment_string";

    public static CommunityPageFragment newInstance(String sectionNumber) {
        CommunityPageFragment fragment = new CommunityPageFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.fragment_community_page, container, false);
        return newView;
    }
}
