Android-SingleInputForm
=======================

A single EditText instead of a classical form.

This Library is a library implementation of  [flavienlaurent's "Single input form"][1]

_Very inspired by the [Minimal Format Interface][2]._

Implement any form with only one EditText. The idea here is to pack a multiple input form in one field. As a result, the user input is easier and a more fluent process.

Also, [TextSwitcher][3] got completely rediscovered to animate the form: title, error message. It's very basic but simple to use.

Here is a video demonstrating a sample form:
http://youtu.be/A99g6NDvn_w

Screenshots
-----------

| E-mail input | Password error | Date picker |
|:-:|:-:|:-:|
| ![Date picker](http://heinrichreimersoftware.com/static/files/screenshots/2014-06-12-21-19-17.png) | ![Date picker](http://heinrichreimersoftware.com/static/files/screenshots/2014-06-12-21-19-50.png) | ![Date picker](http://heinrichreimersoftware.com/static/files/screenshots/2014-06-12-21-20-34.png) |
| [_TextStep.java_][TS] | [_TextStep.java_][TS] | [_DateStep.java_][DS] |

Usage
-----
### Gradle
If you are using Gradle just add maven central repository and add this line to your projects ```build.gradle```

	compile 'com.heinrichreimersoftware.singleinputform:library:1.0'

### Usage
Just let your _Activity_ extend [_SingleInputFormActivity.java_][SIFA] and implement the abstract methods:

```
public class MainActivity extends SingleInputFormActivity{
    private static final String DATA_KEY_EXAMPLE = "example";
    
    @Override
    protected List<Step> getSteps(Context context){
        List<Step> steps = new ArrayList<Step>();
        
        steps.add(
            new TextStep(context, DATA_KEY_EXAMPLE, InputType.TYPE_CLASS_TEXT, R.string.example, R.string.example_error, R.string.example_details)
        );
        
        ...
        
        return steps;
    }
    
    @Override
    protected void onFormFinished(Bundle data){
        String text = TextStep.text(data, DATA_KEY_EXAMPLE);
        ...
    }
}
```

### Customisation
You can customise the form's style in your styles.xml:

```
<resources>
    <style name="AppTheme" parent="android:Theme.Holo.Light">
        <item name="sifStyle">@style/AppTheme.SingleInputFormStyle</item><!-- reference -->
    </style>
    
    <style name="AppTheme.SingleInputFormStyle" parent="SingleInputFormStyle">
        <item name="sifButtonNextIcon">@drawable/ic_action_next</item><!-- drawable -->
        <item name="sifButtonFinishIcon">@drawable/ic_action_finish</item><!-- drawable -->
        
        <item name="sifTextFieldBackgroundColor">#992044</item><!-- color -->
        <item name="sifProgressBackgroundColor">#67132B</item><!-- color -->
        <item name="sifEditTextBackgroundColor">#a94054</item><!-- color -->
        
        <item name="sifTitleTextColor">#561529</item><!-- color -->
        <item name="sifDetailsTextColor">#561529</item><!-- color -->
        <item name="sifInputTextColor">@android:color/white</item><!-- color -->
        <item name="sifErrorTextColor">#561529</item>
        
        <item name="sifBetterPickerStyle">@style/AppTheme.BetterPickerTheme</item><!-- reference -->
    </style>
    
    <style name="AppTheme.BetterPickerTheme" parent="BetterPickersDialogFragment.Light">
        <!-- See https://github.com/derekbrameyer/android-betterpickers#theming for more information -->
        <item name="bpTitleDividerColor">#67132B</item>
        <item name="bpKeyboardIndicatorColor">#67132B</item>
    </style>
</resources>
```

License
-------

    Copyright 2013 Heinrich Reimer

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
    
[1]: https://github.com/flavienlaurent/singleinputform
[2]: https://github.com/codrops/MinimalForm
[3]: http://developer.android.com/reference/android/widget/TextSwitcher.html
[TS]: https://github.com/HeinrichReimer/Android-SingleInputForm/blob/master/library/src/main/java/com/heinrichreimersoftware/singleinputform/steps/TextStep.java
[DS]: https://github.com/HeinrichReimer/Android-SingleInputForm/blob/master/library/src/main/java/com/heinrichreimersoftware/singleinputform/steps/DateStep.java
[SIFA]: https://github.com/HeinrichReimer/Android-SingleInputForm/blob/master/library/src/main/java/com/heinrichreimersoftware/singleinputform/SingleInputFormActivity.java
