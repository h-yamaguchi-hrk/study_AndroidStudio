package com.example.study_androidstudio;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private static final String TAG = "DebugPractice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        // 問題1：ボタンを押すと「10 + 20 = 30」と表示したいのに、なぜか値がおかしくなる
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
        // バグ：本来は (a + b) ですが、練習用に multiplier を掛けています
        int total = (a + b);
        return total;
    }

    // --- 問題2のバグメソッド ---
    private int runLoopAndSum() {
        int sum = 0;
        // 【デバッグワーク】for文の中にブレークポイントを置き、ステップ実行（F8）で変数の変化を追え
        for (int i = 1; i <= 5; i++) {
            sum += i;
            // バグ：i == 3 のときだけ合計を2倍にする謎の処理
//            if (i == 3) {
//                sum *= 2;
//            }
        }
        return sum;
    }

    // --- 問題3のバグメソッド ---
    private void causeCrash() {
        // 【デバッグワーク】アプリが落ちた後、Logcatを開いて「FATAL EXCEPTION」を探せ
        List<String> textList = new ArrayList<>();
        // バグ：nullオブジェクトに対してアクセスしているためクラッシュする
        int size = textList.size();
        Log.d(TAG, "サイズは: " + size);
    }
}
