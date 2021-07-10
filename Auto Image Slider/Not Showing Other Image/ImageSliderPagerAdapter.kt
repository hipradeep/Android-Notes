


class ImageSliderPagerAdapter(context: Context, imageList: List<String>) :
    PagerAdapter() {
    var context: Context? = null
    private var inflater: LayoutInflater? = null
    var imageList: List<String>? = null
    init {
        try {
            this.context = context
            this.imageList = imageList
            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        } catch (e: Exception) {
        }
    }

    override fun getCount(): Int {
        return imageList!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        val inflater: LayoutInflater = LayoutInflater.from(context).context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView: View=inflater.inflate(R.layout.adapter_baner_list,parent,  false)

     //   val itemView: View = View.inflate(context, R.layout.adapter_baner_list, null)
        val imageView = itemView.findViewById<ImageView>(R.id.img_view)

        imageView.scaleType = ImageView.ScaleType.FIT_XY
        //imageView.setImageResource(imageList.get(position).photoUrl);
        if (imageList!![position].isNotEmpty()) {
           Log.e("TAG", imageList!![position])
            context?.let {
                Glide.with(it).load(imageList!![position]).placeholder(R.drawable.ic_logo).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
            }
        }
        parent.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }


}




