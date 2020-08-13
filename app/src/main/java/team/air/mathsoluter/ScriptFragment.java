package team.air.mathsoluter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
//import com.myscript.iink.Editor;
//import com.myscript.iink.uireferenceimplementation.EditorView;

import androidx.fragment.app.Fragment;

import com.myscript.iink.Configuration;
import com.myscript.iink.ContentPackage;
import com.myscript.iink.ContentPart;
import com.myscript.iink.ConversionState;
import com.myscript.iink.Editor;
import com.myscript.iink.Engine;
import com.myscript.iink.IEditorListener;
import com.myscript.iink.PointerType;
import com.myscript.iink.uireferenceimplementation.EditorView;
import com.myscript.iink.uireferenceimplementation.FontUtils;
import com.myscript.iink.uireferenceimplementation.InputController;

//import team.air.mathsoluter.Activities.CustomMathView.CustomMathView;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import team.air.mathsoluter.MyScript.MySctiptManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScriptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScriptFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditorView editorView;
    Editor editor;

    public ScriptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScriptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScriptFragment newInstance(String param1, String param2) {
        ScriptFragment fragment = new ScriptFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_script, container, false);
        editorView = thisView.findViewById(R.id.editor_view);
        Engine engine = MySctiptManager.getEngine();

        Configuration conf = engine.getConfiguration();
        String confDir = "zip://" + thisView.getContext().getPackageCodePath() + "!/assets/conf";
        conf.setStringArray("configuration-manager.search-path", new String[]{confDir});
        String tempDir = thisView.getContext().getExternalFilesDir(null).getPath() + File.separator + "tmp";
        conf.setString("content-package.temp-folder", tempDir);

        AssetManager assetManager = thisView.getContext().getApplicationContext().getAssets();
        Map<String, Typeface> typefaceMap = FontUtils.loadFontsFromAssets(assetManager);
        editorView.setTypefaces(typefaceMap);

        editorView.setEngine(engine);
        editorView.setInputMode(InputController.INPUT_MODE_FORCE_PEN);

        editor = editorView.getEditor();
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
        final Context context = thisView.getContext();
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

        Button button = thisView.findViewById(R.id.convert_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConversionState[] supportedStates = editor.getSupportedTargetConversionStates(null);
                if (supportedStates.length > 0)
                    editor.convert(null, supportedStates[0]);
            }
        });
        return thisView;
    }

}