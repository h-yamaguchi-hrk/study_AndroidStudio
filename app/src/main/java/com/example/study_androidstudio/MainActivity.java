package com.example.study_androidstudio;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private static final String TAG = "DebugPractice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        // 問題1：ボタンを押すと「10 + 20 = 30」と表示したいのに、なぜか「200」になる
        findViewById(R.id.btnProblem1).setOnClickListener(v -> {
            int result = calculateTotal(10, 20);
            tvResult.setText("計算結果: " + result);
        });

        // 問題2：ボタンを押すと「1から5まで足した結果（15）」を表示したいのに、奇妙な値になる
        findViewById(R.id.btnProblem2).setOnClickListener(v -> {
            int result = runLoopAndSum();
            tvResult.setText("ループ合計: " + result);
        });

        // 問題3：ボタンを押すとアプリが強制終了（クラッシュ）する
        findViewById(R.id.btnProblem3).setOnClickListener(v -> {
            causeCrash();
        });
    }

    // --- 問題1のバグメソッド ---
    private int calculateTotal(int a, int b) {
        Log.d(TAG, "calculateTotalが呼び出されました。a=" + a + ", b=" + b);
        // 【デバッグワーク】この行にブレークポイントを置き、変数aとbの中身を確認せよ
        int wrongMultiplier = 10;
        int total = (a + b);
        return total;
    }

    // --- 問題2のバグメソッド ---
    private int runLoopAndSum() {
        int sum = 0;
        // 【デバッグワーク】for文の中にブレークポイントを置き、ステップ実行（F8）で変数の変化を追え
        // 【応用】条件付きブレークポイントで「i == 3」のときのsumの値を確認せよ
        for (int i = 1; i <= 5; i++) {
            sum += i;
//            if (i == 3) {
//                sum *= 2; // 予期せぬおかしな計算が紛れ込んでいる？
//            }
        }
        return sum;
    }

    // --- 問題3のバグメソッド ---
    private void causeCrash() {
        // 【デバッグワーク】アプリが落ちた後、Logcatを開いて「FATAL EXCEPTION」を探せ
        // エラーログの何行目にあなたのコード（MainActivity.java）が指摘されているか特定せよ
        List<String> textList = null;
        int size = textList.size(); // nullオブジェクトに対してアクセスしているためクラッシュする
        Log.d(TAG, "サイズは: " + size);
    }
}
