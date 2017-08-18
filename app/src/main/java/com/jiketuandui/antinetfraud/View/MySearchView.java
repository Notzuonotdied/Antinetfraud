package com.jiketuandui.antinetfraud.View;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiketuandui.antinetfraud.R;

/**
 * Created by Notzuonotdied on 2016/10/26.
 * 自定义searchview
 */
public class MySearchView extends LinearLayout implements View.OnClickListener {

    private EditText etInput;
    private ImageView ivDelete;
    private Button btnSearch;
    private Context mContext;
    private SearchViewListener mListener;

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.search_view_style, this);
        initViews();
    }

    /**
     * 设置搜索回调接口
     *
     * @param listener 监听者
     */
    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    private void initViews() {
        etInput = (EditText) findViewById(R.id.search_et_input);
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
        btnSearch = (Button) findViewById(R.id.search_btn);

        ivDelete.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        etInput.addTextChangedListener(new EditChangedListener());
        etInput.setOnClickListener(this);
        etInput.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                notifyStartSearching(etInput.getText().toString());
            }
            return true;
        });
    }

    /**
     * 通知监听者 进行搜索操作
     *
     * @param text 用户输入的信息
     */
    private void notifyStartSearching(String text) {
        if (mListener != null) {
            mListener.onSearch(text);
            mListener.onQueryTextSubmit(text);
            mListener.onQueryTextChange(text);
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_delete:
                etInput.setText("");
                ivDelete.setVisibility(GONE);
                break;
            case R.id.search_btn:
                mListener.onQueryTextSubmit(etInput.getText().toString());
                break;
            //((Activity) mContext).finish();
        }
    }

    public void setInputString(String s) {
        etInput.setText(s);
    }

    public void toSubmit(String s) {
        etInput.setText(s);
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mListener.onQueryTextSubmit(s);
    }

    /**
     * search view回调方法
     */
    public interface SearchViewListener {
        void onSearch(String text);

        void onQueryTextSubmit(String text);

        void onQueryTextChange(String text);
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
                ivDelete.setVisibility(VISIBLE);
            } else {
                ivDelete.setVisibility(GONE);
            }
            if (mListener != null) {
                mListener.onQueryTextChange(etInput.getText().toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}