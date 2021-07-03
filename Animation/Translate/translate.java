//Method #1

Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate.xml);
findById(R.id.element).startAnimation(animation);

//Method #2
// Add Listener On animation

Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate.xml);
animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                          
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


//Method #3
// Create Translate Animation in java

 DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

 TranslateAnimation animationLogo = new TranslateAnimation(-width, 0, 0, 0);
animationLogo.setDuration(800);
 animationLogo.setFillAfter(true);
 findById(R.id.element).startAnimation(animationLogo);

