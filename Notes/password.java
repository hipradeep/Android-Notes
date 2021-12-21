 boolean isPasswordVisible = false;


        btn_showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isPasswordVisible) {
                    et_password.setTransformationMethod(null);
                    btn_showPassword.setImageResource(R.drawable.eye_black);
                    isPasswordVisible = true;
                } else {
                    et_password.setTransformationMethod(new PasswordTransformationMethod());
                    btn_showPassword.setImageResource(R.drawable.eye_grey);
                    isPasswordVisible = false;
                }
            }
        });
