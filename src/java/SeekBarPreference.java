import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Classe implémentant SeekBar.OnSeekBarChangeListener afin d'afficher des SeekBar dans la fenêtre de préférences.
 *
 * @author Francis Chabot
 * @version 1.0
 * @see Preference
 * @see AttributeSet
 * @see SeekBar
 * @see TextView
 */
public class SeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {
    private int seekBarProgress;
    private int seekBarMaxValue;

    public SeekBarPreference(Context context) {
        this(context, null, 0, 0);
    }

    public SeekBarPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        seekBarProgress = 1;

        // Obtenir la valeur de l'attribut xml maxValue définit lors de la déclaration du SeekBar dans le xml des préférences
        TypedArray xmlAttrs = context.obtainStyledAttributes(attrs, R.styleable.SeekBarPreference, defStyleAttr, defStyleRes);

        try {
            seekBarMaxValue = xmlAttrs.getInteger(R.styleable.SeekBarPreference_maxValue, 2);
            if (seekBarMaxValue < 2) seekBarMaxValue = 2;

        } catch(UnsupportedOperationException e) {
            seekBarMaxValue = 2;
        }
        finally {
            xmlAttrs.recycle();
        }

        // Fichier xml définissant le layout des SeekBar
        setLayoutResource(R.layout.preference_seekbar);
    }

    /**
     * Fonction permettant de modifier la valeur courante du SeekBar
     *
     * @param pValue entier compris entre 1 et seekBarMaxValue représentant la position courante de la poignée du SeekBar
     */
    private void setValue(int pValue) {
        int value = ((pValue >= 1 && pValue <= seekBarMaxValue) ? pValue : 1);

        if (shouldPersist()) persistInt(value);

        if (pValue != seekBarProgress) {
            seekBarProgress = value;
            notifyChanged();
        }
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        TextView seekBarTitle = (TextView) view.findViewById(R.id.seekBarTitle);
        seekBarTitle.setText(this.getTitle());

        TextView seekBarMin = (TextView) view.findViewById(R.id.seekBarMin);
        seekBarMin.setText("1");

        TextView seekBarMax = (TextView) view.findViewById(R.id.seekBarMax);
        seekBarMax.setText(String.valueOf(seekBarMaxValue));

        SeekBar preferenceSeekBar = (SeekBar) view.findViewById(R.id.preferenceSeekBar);
        preferenceSeekBar.setMax(seekBarMaxValue - 1);
        preferenceSeekBar.setProgress(seekBarProgress - 1);

        preferenceSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) return;

        setValue(progress + 1);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // empty
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // empty
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(seekBarProgress) : (Integer) defaultValue);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 1);
    }
}
