   //this is the original Google Drive link to the image
   // image must be sharable
    String driveImageUrl="https://drive.google.com/file/d/1-_yIiIMikp7_NE1LmAGWD5hos-i1ZYyV/view?usp=sharing";

    //you have to get the part of the link 0B9nFwumYtUw9Q05WNlhlM2lqNzQ
    String[] p=driveImageUrl.split("/");

     Log.e("TAG", "Image Id : "+p[5]);

    //Create the new image link
    String imageLink="https://drive.google.com/uc?export=download&id="+p[5];
     Log.e("TAG","Full link : "+ imageLink);
    ImageView imageView = (ImageView) findViewById(R.id.imageView);

     //using picasso
    Picasso.with(YourActivity.this).load(imageLink).into(imageView);

    // Or use Glide
    Glide.with(this).load(imageLink).into(imageView);

    
