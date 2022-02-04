package app.ikd9684.android.study.custom_view_study

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged

class EditTextWithMessageAndClearButton(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)

    private val editText: EditText
    private val textView: TextView
    private val btnClear: ImageButton

    private var useClearButton = false
    private var useErrorMessage = false

    init {
        View.inflate(getContext(), R.layout.edittext_with_message_and_clearbutton, this)

        editText = findViewById(R.id.etInput)
        textView = findViewById(R.id.tvErrorMessage)
        btnClear = findViewById(R.id.btnClear)
        btnClear.isVisible = false

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EditTextWithMessageAndClearButton,
            0,
            0
        ).apply {
            try {
                useClearButton =
                    getBoolean(R.styleable.EditTextWithMessageAndClearButton_useClearButton, false)
                useErrorMessage =
                    getBoolean(R.styleable.EditTextWithMessageAndClearButton_useErrorMessage, false)

                textView.isVisible = useErrorMessage

            } finally {
                recycle()
            }
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            btnClear.isVisible = useClearButton && hasFocus && editText.text.isNullOrEmpty().not()
        }
        editText.doOnTextChanged { text, _, _, _ ->
            btnClear.isVisible = useClearButton && text.isNullOrEmpty().not()
        }

        btnClear.setOnClickListener {
            editText.text.clear()
        }
    }

    fun setInputText(string: String?) {
        editText.text.clear()
        editText.text.append(string)
    }

    fun getInputText(): CharSequence? {
        return editText.text
    }

    fun setErrorMessage(string: String?) {
        textView.text = string
    }

    fun setErrorMessage(@StringRes id: Int, vararg formatArgs: Any) {
        textView.text = context.getString(id, formatArgs)
    }
}
