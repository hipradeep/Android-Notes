
class HomeFragment : Fragment() {
    var s: String = "https://s3-ap-southeast-1.amazonaws.com/zoomcar/images/original/3a036b7e841c0da948fa7cf23d649bc050a12644.png"
    var s2: String = "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_960_720.jpg"

    private lateinit var sliderimaglist: List<String>
    var count = 0
    private val mInterval = 5000
    private var mHandler: Handler? = null

    var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                updateStatus() //this function can change value of mInterval.
            } finally {
                mHandler?.postDelayed(this, mInterval.toLong())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_home, container, false)

        mHandler = Handler()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Slider Data
        sliderimaglist = listOf(s, s2, s, s2)

        val imageSliderPagerAdapter = context?.let { ImageSliderPagerAdapter(it, sliderimaglist) }

        viewPager.adapter = imageSliderPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                count = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun onStop() {
        super.onStop()
          stopRepeatingTask()
    }

    override fun onPause() {
        super.onPause()
         stopRepeatingTask()
    }

    private fun startRepeatingTask() {
        mStatusChecker.run()
    }

    private fun stopRepeatingTask() {
        mHandler!!.removeCallbacks(mStatusChecker)
    }

    fun updateStatus() {
        if (count >= sliderimaglist.size) {
            count = 0
        }
        viewPager.currentItem = count
        count++
    }

    override fun onResume() {
        super.onResume()
        startRepeatingTask()
    }

}
