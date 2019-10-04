package com.lapaksembako.app.helper;

import com.lapaksembako.app.fragments.dummy.DummyContent;
import com.lapaksembako.app.model.Item;

public interface OnListFragmentInteractionListener {

    public static final int SUCCESS = 1;
    void onListFragmentInteraction(Item item);
    void onListItemDelete(int code);
}
