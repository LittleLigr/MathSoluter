package team.air.mathsoluter.Activities.CustomKeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myscript.iink.Configuration;
import com.myscript.iink.ContentPackage;
import com.myscript.iink.ContentPart;
import com.myscript.iink.ConversionState;
import com.myscript.iink.Editor;
import com.myscript.iink.Engine;
import com.myscript.iink.IEditorListener;
import com.myscript.iink.MimeType;
import com.myscript.iink.uireferenceimplementation.EditorView;
import com.myscript.iink.uireferenceimplementation.FontUtils;
import com.myscript.iink.uireferenceimplementation.InputController;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import team.air.mathsoluter.MyScript.MySctiptManager;
import team.air.mathsoluter.R;

public class CustomKeyboardMyScriptTemplate extends CustomKeyboardTemplate {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.custom_keyboard_myscript_fragment, container, false);
        //keyboardView = fragmentView.findViewById(R.id.keyboardViewTemplate);

        final EditorView editorView = fragmentView.findViewById(R.id.editor_view);
        Engine engine = MySctiptManager.getEngine();

        Configuration conf = engine.getConfiguration();
        String confDir = "zip://" + fragmentView.getContext().getPackageCodePath() + "!/assets/conf";
        conf.setStringArray("configuration-manager.search-path", new String[]{confDir});
        String tempDir = fragmentView.getContext().getExternalFilesDir(null).getPath() + File.separator + "tmp";
        conf.setString("content-package.temp-folder", tempDir);

        AssetManager assetManager = fragmentView.getContext().getApplicationContext().getAssets();
        Map<String, Typeface> typefaceMap = FontUtils.loadFontsFromAssets(assetManager);
        editorView.setTypefaces(typefaceMap);

        editorView.setEngine(engine);
        editorView.setInputMode(InputController.INPUT_MODE_FORCE_PEN);

        final Editor editor = editorView.getEditor();
        editor.addListener(new IEditorListener(){

            @Override
            public void partChanging(Editor editor, ContentPart contentPart, ContentPart contentPart1) {

            }

            @Override
            public void partChanged(Editor editor) {

            }

            @Override
            public void contentChanged(Editor editor, String[] strings) {

            }

            @Override
            public void onError(Editor editor, String s, String s1) {

            }
        });

        final String[] partTypes = engine.getSupportedPartTypes();
        final Context context = fragmentView.getContext();
        File file = new File(context.getExternalFilesDir(null), "file");
        try
        {
            ContentPackage newPackage = editor.getEngine().createPackage(file);
            final ContentPart newPart = newPackage.createPart("Math");
            // Choose type of content (possible values are: "Text Document", "Text", "Diagram", "Math", "Drawing" and "Raw Content")
            editorView.post(new Runnable()
            {
                @Override
                public void run()
                {
                    editorView.getRenderer().setViewOffset(0, 0);
                    editorView.getRenderer().setViewScale(1);
                    editorView.setVisibility(View.VISIBLE);
                    editor.setPart(newPart);
                }
            });

        }   catch (IOException e)
        {
            Toast.makeText(context, "Failed to create package", Toast.LENGTH_LONG).show();
        }
        catch (IllegalArgumentException e)
        {
            Toast.makeText(context, "Package already opened", Toast.LENGTH_LONG).show();
        }


        editorView.setVisibility(View.VISIBLE);
        editorView.requestFocus();
        // EditorView view = thisView.findViewById(R.id.myscript);
        // view.setInputMode(InputController.INPUT_MODE_FORCE_PEN);
        // CustomMathView mathView = thisView.findViewById(R.id.katexView);
        //mathView.setDisplayText("$$_frac{4}{5}hello\nlets go!\nboy$$");
        // MainActivity.mCustomKeyboard.registerEditText((EditText) thisView.findViewById(R.id.scriptTextView));

        Button button = fragmentView.findViewById(R.id.convert_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConversionState[] supportedStates = editor.getSupportedTargetConversionStates(null);
                if (supportedStates.length > 0)
                    editor.convert(null, supportedStates[0]);
            }
        });

        Button clear = fragmentView.findViewById(R.id.clear_myscript_button);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
            }
        });

        Button insert = fragmentView.findViewById(R.id.insert_myscript_button);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    EditText editText = host.findViewById(R.id.scriptEditText);
                    editText.setText(editText.getText().insert(editText.getSelectionEnd(),
                            editor.export_(editor.getRootBlock(), MimeType.LATEX)
                    ));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        if(callContainer!=null)
            callContainer.call();
        return fragmentView;
    }

    @Override
    public void init(Activity host, int layoutid, View view) {
        this.viewPager = view;
        this.host = host;
        this.host .getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
