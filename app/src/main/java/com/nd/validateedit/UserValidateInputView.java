package com.nd.validateedit;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 用户输入验证码
 * Created by Sharon on 2017/6/29.
 */

public class UserValidateInputView extends LinearLayout implements TextWatcher,
        View.OnFocusChangeListener,View.OnKeyListener,View.OnClickListener{

    private Context mContext;
    private ArrayList<EditText> editTextArray = new ArrayList<>();
    private int index = 0;
    private OnFocusListener onFocusListener;
    private boolean isFocused = false;
    private InputMethodManager inputManager;

    private int current = 0;
    boolean isDelete = false;

    boolean isBeingDelete = false;

    public void setOnFocusListener(OnFocusListener onFocusListener) {
        this.onFocusListener = onFocusListener;
    }

    public UserValidateInputView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public UserValidateInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public UserValidateInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public void initView(){
        inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        View view = inflate(mContext,R.layout.layout_input_validate,this);
        view.setOnClickListener(this);
        editTextArray.add((EditText)view.findViewById(R.id.ed_pwd_1));
        editTextArray.add((EditText)view.findViewById(R.id.ed_pwd_2));
        editTextArray.add((EditText)view.findViewById(R.id.ed_pwd_3));
        editTextArray.add((EditText)view.findViewById(R.id.ed_pwd_4));
        editTextArray.add((EditText)view.findViewById(R.id.ed_pwd_5));
        editTextArray.add((EditText)view.findViewById(R.id.ed_pwd_6));

        for (int i = 0; i< editTextArray.size(); i++){
            editTextArray.get(i).addTextChangedListener(this);
            editTextArray.get(i).setTag(i);

            editTextArray.get(i).setFocusableInTouchMode(false);
            editTextArray.get(i).setOnFocusChangeListener(this);
            editTextArray.get(i).setBackground(null);
            editTextArray.get(i).setCursorVisible(true);
//            editTextArray.get(i).setFocusable(false);

            editTextArray.get(i).setOnKeyListener(this);
//            editTextArray.get(i).setFilters(new InputFilter[]{this});
        }

        editTextArray.get(0).setFocusableInTouchMode(true);


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.e("before", "before:" + start);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if (count > 0){
//            if (index != editTextArray.size()-1){
////                editTextArray.get(index).clearFocus();
//                index++;
//                editTextArray.get(index).requestFocus();
//                //下一个editText获取焦点
//            }else{
//                return;
//            }
//        }
//        else{
//            if (index != 0){
////                editTextArray.get(index).clearFocus();
//                index--;
//                editTextArray.get(index).requestFocus();
//            }
//        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        editTextArray.get(index).setFocusableInTouchMode(false);

        if (s.length() == 1) {
            if (index != editTextArray.size()-1){
                index++;
                //下一个editText获取焦点
                editTextArray.get(index).setFocusableInTouchMode(true);

                editTextArray.get(index).requestFocus();
            }else{
                return;
            }
        }else{
//            if (index != 0){
//                index--;
//                editTextArray.get(index).requestFocus();
//            }
        }
        Toast.makeText(mContext,s.toString(),Toast.LENGTH_SHORT).show();
    }


    public String getText(){
        StringBuffer res = new StringBuffer();
        for (EditText t : editTextArray){
            res.append(t.getText());
        }
        return res.toString();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) current = (int)v.getTag();
        if (!isDelete && hasFocus && ((EditText)v).length() == 1){
            editTextArray.get(index).setFocusableInTouchMode(false);
            if (index == editTextArray.size()-1) return;
            index ++;
            editTextArray.get(index).setFocusableInTouchMode(true);
            editTextArray.get(index).requestFocus();
        }
        Log.e("index", "which:" + hasFocus + " - " + current);
//        if (!hasFocus){
//            index = (int)v.getTag();
//            editTextArray.get(index).setSelection(editTextArray.get(index).length());
//        }
//        if (!hasFocus){
//            int tag = (int)v.getTag();
//            if (tag == index) return;
//            editTextArray.get(index).requestFocus();
//            editTextArray.get(index).setSelection(editTextArray.get(index).length());
//        }
    }



    @Override
    public void onClick(View v) {
        //显示软键盘
        if (inputManager != null){
            inputManager.showSoftInput(editTextArray.get(index),0,null);
        }
        editTextArray.get(index).requestFocus();
        editTextArray.get(index).setSelection(editTextArray.get(index).length());
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (isBeingDelete) return false;
            else {
                isBeingDelete = true;
                thread();
                if (editTextArray.get(index).length() == 0) {
                    editTextArray.get(index).setFocusableInTouchMode(false);
                    if (index == 0) return true;
                    isDelete = true;
                    index--;
                    editTextArray.get(index).setFocusableInTouchMode(true);
                    editTextArray.get(index).requestFocus();
                    editTextArray.get(index).setText("");
                }
            }
        }
        return false;
    }



    public interface OnFocusListener{
        public void onFocuseChanged(boolean isFocused);
    }


    private void thread(){
        new Handler(mContext.getMainLooper()).postDelayed(new Runnable()
        {
            public void run()
            {
                isBeingDelete = false;
            }
        }, 500);
    }
}
