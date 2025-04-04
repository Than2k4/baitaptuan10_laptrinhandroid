package com.example.bluetooth_socket;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button btnPaired;
    ListView listDanhSach;
    public static int REQUEST_BLUETOOTH = 1;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các thành phần UI
        btnPaired = findViewById(R.id.btnTimthietbi);
        listDanhSach = findViewById(R.id.listTb);

        // Khởi tạo Bluetooth Adapter
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (myBluetooth == null) {
            Toast.makeText(getApplicationContext(), "Thiết bị không hỗ trợ Bluetooth", Toast.LENGTH_LONG).show();
            finish();
        } else if (!myBluetooth.isEnabled()) {
            // Yêu cầu bật Bluetooth nếu chưa bật
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, REQUEST_BLUETOOTH);
        }

        // Khi nhấn nút tìm thiết bị đã ghép đôi
        btnPaired.setOnClickListener(v -> pairedDevicesList());
    }

    private void pairedDevicesList() {
        // Kiểm tra quyền Bluetooth
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress()); // Lấy tên và địa chỉ thiết bị
            }
        } else {
            Toast.makeText(getApplicationContext(), "Không tìm thấy thiết bị kết nối.", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listDanhSach.setAdapter(adapter);

        // Khi người dùng click vào một thiết bị trong danh sách
        listDanhSach.setOnItemClickListener((parent, view, position, id) -> {
            // Lấy địa chỉ của thiết bị đã chọn
            String deviceAddress = list.get(position).split("\n")[1];
            // Gửi địa chỉ thiết bị đến BlueControl Activity
            Intent intent = new Intent(MainActivity.this, BlueControl.class);
            intent.putExtra(EXTRA_ADDRESS, deviceAddress);
            startActivity(intent);
        });
    }
}
