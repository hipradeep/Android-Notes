 <!--Scale Animation, Pass View -->
  setScaleOnButton(view);



<!--Scale Animation, put it in Base Activity-->
 
public void setScaleOnButton(View view){
        Animation scaleAnim = AnimationUtils.loadAnimation(context, R.anim.scale_anim);
        view.startAnimation(scaleAnim);
    }





