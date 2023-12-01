package com.example.app;
import android.os.Bundle;
import android.util.Log;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class MainActivity2 extends AppCompatActivity {

    private Socket socket;
    private EditText editText;
    private Button button;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editText = findViewById(R.id.inputEditText);
        button = findViewById(R.id.sendButton);
        tv=findViewById(R.id.messageTextView);

        try {
            socket = IO.socket("http://10.24.10.0:3000/");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    // Khi kết nối thành công
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String message = editText.getText().toString();
                            socket.emit("chat message", message);
                            editText.setText("");
                        }
                    });
                }
            }).on("chat message", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    final String message = (String) args[0];
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(message);
                        }
                    });
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}