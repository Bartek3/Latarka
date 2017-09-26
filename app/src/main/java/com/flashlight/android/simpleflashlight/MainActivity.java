package com.flashlight.android.simpleflashlight;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    ImageButton button; //Zadeklarowanie przycisku włączającego i wyłączającego latarkę
    private Camera camera; //Zadeklarowanie zmiennej odpowiedzialnej za dostęp do aparatu urządzenia
    private boolean isFlashOn; //Zadeklarowanie zmiennej boolean przechowującej informację, czy latarka aktualnie jest włączona
    Parameters parameters; //Zadeklarowanie zmiennej przechowującej parametry (w tym przypadku aparatu)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        getCamera();
        switchButton();

        //Kosmetyczny warunek - zmienia ono kolor dolnego paska nawigacyjnego na czarny, gdy system na to pozwala
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    turnOffFlash();
                } else {
                    turnOnFlash();
                }
            }
        });
    }

    //Metoda odpowiedzialna za dostęp programu do aparatu urządzenia
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                parameters = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error.", e.getMessage());
            }
        }
    }

    //Metoda odpowiedzialna za włączanie latarki
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || parameters == null) {
                return;
            }

            parameters = camera.getParameters();
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            isFlashOn = true;

            switchButton();
        }

    }

    //Metoda odpowiedzialna za wyłączanie latarki
    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || parameters == null) {
                return;
            }

            parameters = camera.getParameters();
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            isFlashOn = false;

            switchButton();
        }
    }

    //Metoda odpowiedzialna za zmianę wyglądu przycisku  z "on" na "off" i na odwrót
    private void switchButton() {
        button = findViewById(R.id.button);
        if (isFlashOn) {
            button.setBackgroundResource(R.drawable.on);
        } else {
            button.setBackgroundResource(R.drawable.off);
        }
    }
}