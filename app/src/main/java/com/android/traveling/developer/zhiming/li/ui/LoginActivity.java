package com.android.traveling.developer.zhiming.li.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.entity.MyUser;
import com.android.traveling.util.MyCustomDialog;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;

import java.util.regex.Pattern;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：LoginActivity
 * 创建者：HY
 * 创建时间：2018/10/4 8:30
 * 描述：  登录
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText username;
    private EditText password;
    private EditText verified_code;
    private Button btn_login;
    private Button btn_verified_code;
    private inputContent inputContent = new inputContent();

    //输入是否合法
    private boolean usernameValid = false;  //手机号
    private boolean emailValid = false;
    private boolean verifiedCodeValid = false;
    private boolean loginEnable = false;

    //登录方式
    private int loginMode = StaticClass.LOGIN_BY_VERIFIED;
    private boolean passwordMode = true;  //true为手机账号登录 false为邮箱账号登录
    private TextView login_forget_pass;
    private TextView login_by_password;
    private TextView cut_line;
    private TextView login_by_verified;
    private LinearLayout LL_verified;

    private VerifiedTimer verifiedTimer;
    private MyCustomDialog loginDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verifiedTimer = new VerifiedTimer(60000, 1000);
        initView();
    }

    //初始化View
    private void initView() {

        loginDialog = MyCustomDialog.getLoginDialog(this);
        loginDialog.setCancelable(false);  //屏幕外点击无效

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(this);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        btn_verified_code = findViewById(R.id.btn_verified_code);
        btn_verified_code.setOnClickListener(this);

        TextView service_terms = findViewById(R.id.service_terms);
        service_terms.setOnClickListener(this);

        //设置字体
        TextView tv_login = findViewById(R.id.tv_login);
        UtilTools.setDefaultFontType(this, tv_login);

        //忘记密码
        login_forget_pass = findViewById(R.id.login_forget_pass);
        login_forget_pass.setOnClickListener(this);

        username = findViewById(R.id.login_login);
        password = findViewById(R.id.login_password);
        verified_code = findViewById(R.id.login_verified_code);
        cut_line = findViewById(R.id.cut_line);
        LL_verified = findViewById(R.id.LL_verified);
        login_by_password = findViewById(R.id.login_by_password);
        login_by_verified = findViewById(R.id.login_by_verified);
        login_by_password.setOnClickListener(this);
        login_by_verified.setOnClickListener(this);

        //输入账号密码时监听事件
        addTextChangedListener();

        //其他方式登录
        ImageView ic_wechat = findViewById(R.id.ic_wechat);
        ImageView ic_qq = findViewById(R.id.ic_qq);
        ImageView ic_weibo = findViewById(R.id.ic_weibo);
        ic_wechat.setOnClickListener(this);
        ic_qq.setOnClickListener(this);
        ic_weibo.setOnClickListener(this);
    }

    //添加editText输入监听事件
    private void addTextChangedListener() {

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (loginMode) {
                    case StaticClass.LOGIN_BY_VERIFIED:
                        if (s.length() == StaticClass.PHONE_MAX_LENGTH) {
                            usernameValid = true;
                            if (verifiedCodeValid) {
                                setLoginEnabled(true);
                            }
                        } else {
                            if (usernameValid) {
                                usernameValid = false;
                            }
                            if (loginEnable) {
                                setLoginEnabled(false);
                            }
                        }
                        break;
                    case StaticClass.LOGIN_BY_PHONE:
                        if (s.length() == StaticClass.PHONE_MAX_LENGTH) {
                            usernameValid = true;
                            if (password.getText().toString().length() >=
                                    StaticClass.PASSWORD_MIN_LENGTH) {
                                setLoginEnabled(true);
                            }
                        } else {
                            if (usernameValid) {
                                usernameValid = false;
                            }

                            if (loginEnable) {
                                setLoginEnabled(false);
                            }
                        }
                        break;
                    case StaticClass.LOGIN_BY_EMAIL:
                        if (Pattern.matches(StaticClass.EMAIL_REGULAR, s.toString())) {
                            emailValid = true;
                            if (password.getText().toString().length() >=
                                    StaticClass.PASSWORD_MIN_LENGTH) {
                                setLoginEnabled(true);
                            }
                        } else {
                            if (emailValid) {
                                emailValid = false;
                            }

                            if (loginEnable) {
                                setLoginEnabled(false);
                            }
                        }
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verified_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (loginMode) {
                    case StaticClass.LOGIN_BY_VERIFIED:
                        if (s.length() == StaticClass.VERIFIED_CODE_MAX_LENGTH) {
                            verifiedCodeValid = true;
                            if (usernameValid) {
                                setLoginEnabled(true);
                            }
                        } else {
                            if (loginEnable) {
                                setLoginEnabled(false);
                            }

                            if (verifiedCodeValid) {
                                verifiedCodeValid = false;
                            }
                        }
                        break;
                    case StaticClass.LOGIN_BY_PHONE:
                        break;
                    case StaticClass.LOGIN_BY_EMAIL:
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= StaticClass.PASSWORD_MIN_LENGTH) {
                    if (loginMode == StaticClass.LOGIN_BY_PHONE && usernameValid) {
                        setLoginEnabled(true);
                    }
                    if (loginMode == StaticClass.LOGIN_BY_EMAIL && emailValid) {
                        setLoginEnabled(true);
                    }
                } else {

                    if (loginEnable) {
                        setLoginEnabled(false);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //设置登录按钮是否可点击
    private void setLoginEnabled(boolean isEnabled) {
        loginEnable = isEnabled;
        btn_login.setEnabled(isEnabled);
        if (isEnabled) {
            btn_login.setBackgroundResource(R.drawable.blue_bg);
        } else {
            btn_login.setBackgroundResource(R.drawable.btn_gray_bg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Login();
                break;
            case R.id.service_terms:
                startActivity(new Intent(this, ServiceTermsActivity.class));
                break;
            case R.id.login_forget_pass:
                UtilTools.toast(this,"忘记密码");
                break;
            case R.id.ic_wechat:
                UtilTools.toast(this, "微信登录");
                break;
            case R.id.ic_qq:
                UtilTools.toast(this, "qq登录");
                break;
            case R.id.ic_weibo:
                UtilTools.toast(this, "微博登录");
                break;
            case R.id.login_by_password:
                changePasswordMode();
                break;
            case R.id.login_by_verified:
                changeLoginMode();
                break;
            case R.id.btn_verified_code:
                getVerifiedCode();
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }


    //获取验证码
    private void getVerifiedCode() {
        if (usernameValid) {

            BmobSMS.requestSMSCode(username.getText().toString()
                    , StaticClass.BMOB_SMS_TEMPLATE, new QueryListener<Integer>() {

                        @Override
                        public void done(Integer smsId, BmobException ex) {
                            if (ex == null) {//验证码发送成功
                                LogUtil.d("短信id：" + smsId);//用于查询本次短信发送详情
                                verifiedTimer.start();
                                UtilTools.toast(LoginActivity.this, "验证码发送成功，请注意查收！");
                            } else {
                                UtilTools.toastException(LoginActivity.this,ex);
                            }
                        }
                    });


        } else {
            UtilTools.toast(this, "请输入正确的手机号码");
        }
    }

    //登录
    private void Login() {
        loginDialog.show();
        switch (loginMode) {
            case StaticClass.LOGIN_BY_VERIFIED:
                loginByVerifiedCode();
                break;
            case StaticClass.LOGIN_BY_PHONE:
                loginByPhone();
                break;
            case StaticClass.LOGIN_BY_EMAIL:
                loginByEmail();
                break;
        }
    }


    //验证码登录
    private void loginByVerifiedCode() {
        MyUser user = new MyUser();
        user.setMobilePhoneNumber(username.getText().toString());
        user.setNickName(getString(R.string.init_username));
        user.setGender(StaticClass.GENDER_SECRET);
        user.setLiveArea("");
        user.setHasPass(false);
        user.signOrLogin(verified_code.getText().toString(), new SaveListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                if (e == null) {
                    loginDialog.dismiss();
                    sendBroadcast(new Intent(StaticClass.BROADCAST_LOGIN));
                    LoginActivity.this.finish();
                } else {
                    UtilTools.toast(LoginActivity.this, "失败:" + e.getMessage());
                }
            }
        });

    }

    //手机账号登录
    private void loginByPhone() {
        BmobUser.loginByAccount(username.getText().toString()
                , password.getText().toString(), new LogInListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                if(user!=null){
                    loginDialog.dismiss();
                    UtilTools.toast(LoginActivity.this, "登录成功");
                    sendBroadcast(new Intent(StaticClass.BROADCAST_LOGIN));
                    LoginActivity.this.finish();
                } else {
                    UtilTools.toastException(LoginActivity.this,e);
                }
            }
        });
    }

    //邮箱账号登录
    private void loginByEmail() {
        BmobUser.loginByAccount(username.getText().toString(),
                password.getText().toString(), new LogInListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                if(user!=null){
                    loginDialog.dismiss();
                    if (TextUtils.isEmpty(user.getEmail())){
                        UtilTools.toast(LoginActivity.this,"该邮箱账号不存在！");
                    }else {
                        if (user.getEmailVerified()) {
                            sendBroadcast(new Intent(StaticClass.BROADCAST_LOGIN));
                            LoginActivity.this.finish();
                        }else {
                            UtilTools.toast(LoginActivity.this,"登录失败,验证邮箱后才能使用邮箱账号登录");
                        }
                    }
                } else {
                    UtilTools.toastException(LoginActivity.this,e);
                }
            }
        });
    }

    //切换登录方式
    private void changeLoginMode() {
        if (loginMode == StaticClass.LOGIN_BY_VERIFIED) {

            LL_verified.setVisibility(View.INVISIBLE);
            password.setVisibility(View.VISIBLE);
            login_by_password.setVisibility(View.VISIBLE);
            login_forget_pass.setVisibility(View.VISIBLE);
            cut_line.setVisibility(View.VISIBLE);
            login_by_verified.setText(getString(R.string.login_by_verified));
            if (passwordMode) { //由验证码登录换为手机账号登录
                setInputType(StaticClass.LOGIN_BY_PHONE);
                changeLoginEnabled(usernameValid);
            } else {            //由验证码登录换为邮箱账号登录
                setInputType(StaticClass.LOGIN_BY_EMAIL);
                username.setHint(getString(R.string.input_email));
                username.setText(inputContent.email);
                changeLoginEnabled(emailValid);
            }


        } else {
            setInputType(StaticClass.LOGIN_BY_VERIFIED);

            if (passwordMode) {
                inputContent.phoneNumber = username.getText().toString();

            } else {
                inputContent.email = username.getText().toString();
                username.setText(inputContent.phoneNumber);

            }
            LL_verified.setVisibility(View.VISIBLE);
            password.setVisibility(View.INVISIBLE);
            login_by_password.setVisibility(View.GONE);
            login_forget_pass.setVisibility(View.GONE);
            cut_line.setVisibility(View.GONE);
            username.setHint(getString(R.string.input_phone));
            login_by_verified.setText(getString(R.string.login_by_password));

            if (username.getText().toString().length() == StaticClass.PHONE_MAX_LENGTH
                    && verified_code.getText().toString().length() == StaticClass.VERIFIED_CODE_MAX_LENGTH) {
                setLoginEnabled(true);
            } else {
                setLoginEnabled(false);
            }
        }
    }

    //切换登录方式时设置登录按钮是否可点击
    private void changeLoginEnabled(boolean isEnabled) {
        if (password.getText().toString().length() >= 5 && isEnabled) {
            setLoginEnabled(true);
        } else {
            setLoginEnabled(false);
        }
    }

    //切换登录方式
    private void changePasswordMode() {
        switch (loginMode) {
            case StaticClass.LOGIN_BY_EMAIL:
                setInputType(StaticClass.LOGIN_BY_PHONE);
                passwordMode = true;
                login_by_password.setText(getString(R.string.login_by_email));
                username.setHint(getString(R.string.input_phone));

                inputContent.email = username.getText().toString();
                username.setText(inputContent.phoneNumber);
                changeLoginEnabled(usernameValid);
                break;
            case StaticClass.LOGIN_BY_PHONE:
                passwordMode = false;
                setInputType(StaticClass.LOGIN_BY_EMAIL);
                login_by_password.setText(getString(R.string.login_by_phone));
                username.setHint(getString(R.string.input_email));

                inputContent.phoneNumber = username.getText().toString();
                username.setText(inputContent.email);
                changeLoginEnabled(emailValid);
                break;

        }

    }

    //设置username的类型同时设置loginMode
    private void setInputType(int loginMode) {
        this.loginMode = loginMode;
        switch (loginMode) {
            case StaticClass.LOGIN_BY_VERIFIED:
            case StaticClass.LOGIN_BY_PHONE:
                username.setInputType(InputType.TYPE_CLASS_PHONE);
                username.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(11)
                });
                break;
            case StaticClass.LOGIN_BY_EMAIL:
                username.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                username.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(50)
                });
                break;
        }

    }




    class VerifiedTimer extends CountDownTimer {

        private boolean hasSet = false;

        VerifiedTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (!hasSet) {
                btn_verified_code.setBackgroundResource(R.drawable.btn_verified_bg2);
                btn_verified_code.setEnabled(false);
            }
            btn_verified_code.setText(getString(R.string.verifiedMilliSecond,
                    millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            btn_verified_code.setBackgroundResource(R.drawable.btn_verified_bg);
            btn_verified_code.setText("重新发送");
            btn_verified_code.setEnabled(true);
        }
    }


    class inputContent {
        String phoneNumber = "";
        String email = "";
    }
}
