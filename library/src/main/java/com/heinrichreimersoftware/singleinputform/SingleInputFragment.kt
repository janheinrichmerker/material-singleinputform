package com.heinrichreimersoftware.singleinputform


import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.Property
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.heinrichreimersoftware.singleinputform.steps.Step
import java.util.*


abstract class SingleInputFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_single_input_form, container, false)

        loadTheme()

        findViews(view)

        steps = onCreateSteps()

        if (savedInstanceState != null) {
            setupData = savedInstanceState.getBundle(KEY_DATA)
            stepIndex = savedInstanceState.getInt(KEY_STEP_INDEX, 0)
        }

        setupTitle()
        setupInput()
        setupError()
        setupDetails()

        nextButton?.setOnClickListener(nextButtonClickListener)
        errorSwitcher?.setText("")
        updateStep()

        return view
    }

    private val KEY_DATA = "key_data"
    private val KEY_STEP_INDEX = "key_step_index"

    private val PB_PROGRESS_PROPERTY = object : Property<ProgressBar, Int>(Int::class.java, "PB_PROGRESS_PROPERTY") {

        override fun set(pb: ProgressBar, value: Int?) {
            pb.progress = value ?: 0
        }

        override fun get(pb: ProgressBar): Int {
            return pb.progress
        }
    }

    private var steps: List<Step> = ArrayList()
    private var setupData: Bundle? = Bundle()
    private var stepIndex = 0
    private var error: Boolean = false

    private var container: FrameLayout? = null
    private var containerScrollView: ScrollView? = null
    private var innerContainer: LinearLayout? = null
    private var titleSwitcher: TextSwitcher? = null
    private var errorSwitcher: TextSwitcher? = null
    private var detailsSwitcher: TextSwitcher? = null
    private var textField: CardView? = null
    private var inputSwitcher: ViewAnimator? = null
    private var nextButton: ImageButton? = null
    private var progress: ProgressBar? = null
    private var stepText: TextView? = null

    private val nextButtonClickListener = View.OnClickListener { nextStep() }

    private var buttonNextIcon: Drawable? = null
    private var buttonFinishIcon: Drawable? = null

    private var textFieldBackgroundColor = -1
    private var progressBackgroundColor = -1

    private var titleTextColor = -1
    private var detailsTextColor = -1
    private var errorTextColor = -1

//    override fun onBackPressed() {
//        if (stepIndex == 0) {
//            onDestroyView()
//        } else {
//            previousStep()
//        }
//    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            setupData = savedInstanceState.getBundle(KEY_DATA)
            stepIndex = savedInstanceState.getInt(KEY_STEP_INDEX, 0)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        setupData = getCurrentStep().save(setupData)
        outState.putBundle(KEY_DATA, setupData)
        outState.putInt(KEY_STEP_INDEX, stepIndex)
    }

    override fun onPause() {
        hideSoftInput()
        super.onPause()
    }

    protected abstract fun onCreateSteps(): List<Step>

    private fun findViews(rootView: View) {
        container = rootView.findViewById(R.id.container) as FrameLayout
        containerScrollView = rootView.findViewById(R.id.containerScrollView) as ScrollView
        innerContainer = rootView.findViewById(R.id.innerContainer) as LinearLayout
        titleSwitcher = rootView.findViewById(R.id.titleSwitcher) as TextSwitcher
        errorSwitcher = rootView.findViewById(R.id.errorSwitcher) as TextSwitcher
        detailsSwitcher = rootView.findViewById(R.id.detailsSwitcher) as TextSwitcher
        textField = rootView.findViewById(R.id.textField) as CardView
        inputSwitcher = rootView.findViewById(R.id.inputSwitcher) as ViewAnimator
        nextButton = rootView.findViewById(R.id.nextButton) as ImageButton
        progress = rootView.findViewById(R.id.progress) as ProgressBar
        stepText = rootView.findViewById(R.id.stepText) as TextView
        setProgressDrawable()
    }

    protected fun getCurrentStep(): Step {
        return getStep(stepIndex)
    }

    protected fun getStep(position: Int): Step {
        return steps[position]
    }

    private fun loadTheme() {
        /* Default values */
        buttonNextIcon = ContextCompat.getDrawable(context, R.drawable.ic_arrow_forward)
        buttonFinishIcon = ContextCompat.getDrawable(context, R.drawable.ic_done)


        /* Custom values */
        val attrs = intArrayOf(R.attr.colorPrimary, R.attr.colorPrimaryDark, android.R.attr.textColorPrimary, android.R.attr.textColorSecondary, R.attr.sifNextIcon, R.attr.sifFinishIcon)
        val array = activity.obtainStyledAttributes(attrs)

        textFieldBackgroundColor = array.getColor(0, 0)
        progressBackgroundColor = array.getColor(1, 0)
        errorTextColor = array.getColor(2, 0)
        titleTextColor = errorTextColor
        detailsTextColor = array.getColor(3, 0)

        val buttonNextIcon = array.getDrawable(4)
        if (buttonNextIcon != null) {
            this.buttonNextIcon = buttonNextIcon
        }

        val buttonFinishIcon = array.getDrawable(0)
        if (buttonFinishIcon != null) {
            this.buttonFinishIcon = buttonFinishIcon
        }

        array.recycle()
    }

    private fun getAnimation(animationResId: Int, isInAnimation: Boolean): Animation {
        val interpolator: Interpolator

        if (isInAnimation) {
            interpolator = DecelerateInterpolator(1.0f)
        } else {
            interpolator = AccelerateInterpolator(1.0f)
        }

        val animation = AnimationUtils.loadAnimation(context, animationResId)
        animation.interpolator = interpolator

        return animation
    }

    private fun setupTitle() {
        titleSwitcher?.inAnimation = getAnimation(R.anim.slide_in_to_bottom, true)
        titleSwitcher?.outAnimation = getAnimation(R.anim.slide_out_to_top, false)

        titleSwitcher?.setFactory {
            val view = activity.layoutInflater.inflate(R.layout.view_title, titleSwitcher, false) as TextView
            view.setTextColor(titleTextColor)
            view
        }

        titleSwitcher?.setText("")
    }

    private fun setupInput() {
        inputSwitcher?.inAnimation = getAnimation(R.anim.alpha_in, true)
        inputSwitcher?.outAnimation = getAnimation(R.anim.alpha_out, false)

        inputSwitcher?.removeAllViews()
        for (i in steps.indices) {
            inputSwitcher?.addView(getStep(i).view)
        }
    }

    private fun setupError() {
        errorSwitcher?.inAnimation = getAnimation(android.R.anim.slide_in_left, true)
        errorSwitcher?.outAnimation = getAnimation(android.R.anim.slide_out_right, false)

        errorSwitcher?.setFactory {
            val view = activity.layoutInflater.inflate(R.layout.view_error, titleSwitcher, false) as TextView?
            view?.let {
                if (errorTextColor != -1) {
                    view.setTextColor(errorTextColor)
                }
                it
            }
        }

        errorSwitcher?.setText("")
    }

    private fun setupDetails() {
        detailsSwitcher?.inAnimation = getAnimation(R.anim.alpha_in, true)
        detailsSwitcher?.outAnimation = getAnimation(R.anim.alpha_out, false)

        detailsSwitcher?.setFactory {
            val view = activity.layoutInflater.inflate(R.layout.view_details, titleSwitcher, false) as TextView?
            view?.let {
                if (detailsTextColor != -1) {
                    view.setTextColor(detailsTextColor)
                }
                view
            }
        }

        detailsSwitcher?.setText("")
    }

    private fun updateStep() {
        if (stepIndex >= steps.size) {
            hideSoftInput()

            val finishedView = onCreateFinishedView(activity.layoutInflater, container)
            if (finishedView != null) {
                finishedView.alpha = 0f
                finishedView.visibility = View.VISIBLE
                container?.addView(finishedView)
                finishedView.animate()
                        .alpha(1f).duration = resources.getInteger(
                        android.R.integer.config_mediumAnimTime).toLong()
            }

            onFormFinished(setupData)
            return
        }
        updateViews()
        containerScrollView?.smoothScrollTo(0, 0)
    }

    private fun hideSoftInput() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        val v = activity.currentFocus ?: return
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }

    protected fun onCreateFinishedView(inflater: LayoutInflater, parent: ViewGroup?): View? {
        return null
    }

    protected abstract fun onFormFinished(data: Bundle?)

    private fun updateViews() {
        val step = getCurrentStep()

        if (stepIndex + 1 >= steps.size) {
            nextButton?.setImageDrawable(buttonFinishIcon)
            nextButton?.contentDescription = getString(R.string.finish)
            step.updateView(true)
        } else {
            nextButton?.setImageDrawable(buttonNextIcon)
            nextButton?.contentDescription = getString(R.string.next_step)
            step.updateView(false)
        }

        step.restore(setupData)

        setTextFieldBackgroundDrawable()

        inputSwitcher?.displayedChild = stepIndex
        errorSwitcher?.setText("")
        detailsSwitcher?.setText(step.getDetails(context))
        titleSwitcher?.setText(step.getTitle(context))
        stepText?.text = getString(R.string.page_number, stepIndex + 1, steps.size)

        stepText?.setTextColor(detailsTextColor)

        updateProgressbar()
    }

    private fun setTextFieldBackgroundDrawable() {
        if (textFieldBackgroundColor != -1) {
            textField?.setCardBackgroundColor(textFieldBackgroundColor)
        }
    }

    private fun setProgressDrawable() {
        if (progressBackgroundColor != -1) {
            val progressDrawable = progress?.progressDrawable
            progressDrawable?.setColorFilter(progressBackgroundColor, PorterDuff.Mode.SRC_IN)
        }
    }

    private fun updateProgressbar() {
        progress?.max = steps.size * 100
        ObjectAnimator.ofInt(progress, PB_PROGRESS_PROPERTY, stepIndex * 100).start()
    }

    protected fun previousStep() {
        setupData = getCurrentStep().save(setupData)
        stepIndex--
        updateStep()
    }

    protected fun nextStep() {
        val step = getCurrentStep()
        val checkStep = checkStep()
        if (!checkStep) {
            if (!error) {
                error = true
                errorSwitcher?.setText(step.getError(context))
            }
        } else {
            error = false
        }
        if (error) {
            return
        }
        setupData = step.save(setupData)

        stepIndex++
        updateStep()
    }

    private fun checkStep(): Boolean {
        return getCurrentStep().validate()
    }

    fun setInputGravity(gravity: Int) {
        val layoutParams = innerContainer?.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = gravity
        innerContainer?.layoutParams = layoutParams
    }

    fun getInputGravity(): Int {
        val layoutParams = innerContainer?.layoutParams as FrameLayout.LayoutParams
        return layoutParams.gravity
    }

}
