package com.example.bluetooth_socket;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.UUID;

public class BlueControl extends AppCompatActivity {
    ImageButton btnTb1, btnTb2, btnDis;
    TextView txt1, txtMAC;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    String address = null;
    private ProgressDialog progress;
    int flaglamp1 = 0;
    int flaglamp2 = 0;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        // Nhận địa chỉ Bluetooth từ MainActivity
        Intent newint = getIntent();
        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS); // Nhận địa chỉ thiết bị Bluetooth

        // Ánh xạ các thành phần UI
        btnTb1 = findViewById(R.id.btnTb1);
        btnTb2 = findViewById(R.id.btnTb2);
        txt1 = findViewById(R.id.textV1);
        txtMAC = findViewById(R.id.textViewMAC);
        btnDis = findViewById(R.id.btnDisc);

        // Kết nối Bluetooth
        new ConnectBT().execute();

        // Xử lý sự kiện nút bấm
        btnTb1.setOnClickListener(v -> thietTbi1());
        btnTb2.setOnClickListener(v -> thietTbi7());
        btnDis.setOnClickListener(v -> Disconnect());
    }

    // Điều khiển thiết bị 1
    private void thietTbi1() {
        if (btSocket != null) {
            try {
                if (flaglamp1 == 0) {
                    flaglamp1 = 1;
                    btnTb1.setBackgroundResource(R.drawable.tb1on);
                    btSocket.getOutputStream().write("1".toString().getBytes());
                    txt1.setText("Thiết bị số 1 đang bật");
                } else {
                    flaglamp1 = 0;
                    btnTb1.setBackgroundResource(R.drawable.tb1off);
                    btSocket.getOutputStream().write("A".toString().getBytes());
                    txt1.setText("Thiết bị số 1 đang tắt");
                }
            } catch (IOException e) {
                msg("Lỗi kết nối với thiết bị.");
            }
        }
    }

    // Điều khiển thiết bị 7
    private void thietTbi7() {
        if (btSocket != null) {
            try {
                if (flaglamp2 == 0) {
                    flaglamp2 = 1;
                    btnTb2.setBackgroundResource(R.drawable.tb2on);
                    btSocket.getOutputStream().write("7".toString().getBytes());
                    txt1.setText("Thiết bị số 7 đang bật");
                } else {
                    flaglamp2 = 0;
                    btnTb2.setBackgroundResource(R.drawable.tb2off);
                    btSocket.getOutputStream().write("G".toString().getBytes());
                    txt1.setText("Thiết bị số 7 đang tắt");
                }
            } catch (IOException e) {
                msg("Lỗi kết nối với thiết bị.");
            }
        }
    }

    // Ngắt kết nối
    private void Disconnect() {
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                msg("Lỗi ngắt kết nối.");
            }
        }
        finish();
    }

    // Kết nối Bluetooth
    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(BlueControl.this, "Đang kết nối...", "Xin vui lòng đợi...");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    if (ActivityCompat.checkSelfPermission(BlueControl.this
                            , Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                        btSocket.connect();//start connection
                    }
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (!ConnectSuccess) {
                msg("Kết nối thất bại! Kiểm tra thiết bị.");
                finish();
            } else {
                msg("Kết nối thành công.");
                isBtConnected = true;
            }
        }
    }

    // Hiển thị thông báo Toast
    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
