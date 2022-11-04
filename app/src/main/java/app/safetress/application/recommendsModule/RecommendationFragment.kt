package app.safetress.application.recommendsModule


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import app.safetress.application.recommendsModule.recyclerview.adapter.RecommendsAdapter
import app.safetress.application.recommendsModule.recyclerview.entiti.TreeEntityHome
import app.safetress.application.recommendsModule.recyclerview.jsonData.RecommendationProvider
import app.safetress.application.recommendsModule.recyclerview.utils.OnClickListener
import com.example.safetress.R
import com.example.safetress.databinding.FragmentRecommendationBinding
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class RecommendationFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentRecommendationBinding
    private lateinit var mAdapter: RecommendsAdapter
    private lateinit var mGridLayout: GridLayoutManager
    val list = mutableListOf<CarouselItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCarousel()
        initRecyclerView()
    }

    private fun initCarousel() {
        list.add(
            CarouselItem(
                "https://s3-us-west-2.amazonaws.com/cdn01.pucp.education/climadecambios/wp-content/uploads/2020/06/04224953/webclimaDMMA.jpg",
                "Cuidado del arbol"
            )
        )
        list.add(
            CarouselItem(
                "https://static.vecteezy.com/system/resources/previews/006/625/069/non_2x/humans-are-planting-trees-on-the-soil-with-two-hands-environmental-care-concept-by-planting-plants-photo.jpg",
                "Planta"
            )
        )
        list.add(
            CarouselItem(
                "https://www.eimenuts.com/app/uploads/sin-t_tulo-2.jpg",
                "Cuidado del arbol"
            )
        )
        list.add(
            CarouselItem(
                "https://fundacionpernodricardnola.org/blog/wp-content/uploads/2021/06/CharcoBendito-30-1024x683.jpg",
                "Planta un arbol"
            )
        )
        list.add(
            CarouselItem(
                "https://www.radioestacion.com.ar/wp-content/uploads/2021/08/f1280x720-136859_268534_5050-770x470.jpg",
                "Un árbol es un ser que vive para darnos vida."
            )
        )
        list.add(
            CarouselItem(
                "https://www.reddearboles.org/nwlib6/includes/phpthumb/phpThumb.php?src=/imagenes/reforestacion.jpeg&w=700&f=jpeg",
                "Ayuda al planeta"
            )
        )
        list.add(
            CarouselItem(
                "https://images.milenio.com/r_akh9ZbvNqzXkfx4CNOzIEL8IU=/936x566/uploads/media/2019/07/11/plantacion-arboles-cuidado-ninos-acercar.jpg",
                "Los árboles ciertamente tienen corazones."
            )
        )
        binding.carousel.addData(list)
        binding.carousel.start()


        return list.clear()
    }

    private fun initRecyclerView() {
        mAdapter = RecommendsAdapter(this, RecommendationProvider.recommendationJson)
        mGridLayout = GridLayoutManager(
            this.context,
            resources.getInteger(R.integer.main_columns),
            GridLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerViewR.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    override fun onClick(treeEntityHome: TreeEntityHome) {
        intent(treeEntityHome)
    }

    private fun intent(treeEntityHome: TreeEntityHome) {
        val detail = DetailFragment()
        val fragment : Fragment? =
            parentFragmentManager.findFragmentByTag(detail::class.java.simpleName)

        if (fragment !is DetailFragment) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.hostFragment, detail, DetailFragment::class.java.simpleName)
                .addToBackStack(null)
                .commit()
            val bundle = Bundle()
            bundle.putString("name", treeEntityHome.name)
            bundle.putString("description", treeEntityHome.description)
            bundle.putString("photoUrl", treeEntityHome.photoUrl)
            requireFragmentManager().setFragmentResult("key", bundle)
        }
    }


}