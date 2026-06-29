package com.example.study_androidstudio;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private static final String TAG = "DebugPractice";
    private int downloadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        // 問題1：計算バグ
        findViewById(R.id.btnProblem1).setOnClickListener(v -> {
            int result = calculateTotal(10, 20);
            tvResult.setText("計算結果: " + result);
        });

        // 問題2：ループ合計バグ
        findViewById(R.id.btnProblem2).setOnClickListener(v -> {
            int result = runLoopAndSum();
            tvResult.setText("ループ合計: " + result);
        });

        // 問題3：NullPointerExceptionバグ
        findViewById(R.id.btnProblem3).setOnClickListener(v -> {
            causeCrash();
        });

        // 問題4：型変換 (修正済み)
        findViewById(R.id.btnProblem4).setOnClickListener(v -> {
            String userInput = "123a"; 
            processUserInput(userInput);
        });

        // 問題5：文字列比較 (修正済み)
        findViewById(R.id.btnProblem5).setOnClickListener(v -> {
            checkAdminStatus();
        });

        // 問題6：非同期処理の連打対策（修正済み）
        findViewById(R.id.btnProblem6).setOnClickListener(v -> {
            triggerAsyncDownload((Button) v);
        });

        // 問題7：IndexOutOfBoundsExceptionバグ
        findViewById(R.id.btnProblem7).setOnClickListener(v -> {
            accessInvalidIndex();
        });

        // 問題8：浮動小数点の精度バグ
        findViewById(R.id.btnProblem8).setOnClickListener(v -> {
            checkFloatingPointMath();
        });

        // 問題9：StackOverflowErrorバグ
        findViewById(R.id.btnProblem9).setOnClickListener(v -> {
            startInfiniteRecursion(0);
        });
    }

    // --- 問題1〜3 (省略せずに維持) ---
    private int calculateTotal(int a, int b) {
        int wrongMultiplier = 10;
        return (a + b) * wrongMultiplier;
    }

    private int runLoopAndSum() {
        int sum = 0;
        for (int i = 1; i <= 5; i++) {
            sum += i;
            if (i == 3) sum *= 2;
        }
        return sum;
    }

    private void causeCrash() {
        List<String> textList = null;
        int size = textList.size(); 
        Log.d(TAG, "サイズは: " + size);
    }

    // --- 問題4：型変換 (修正済み) ---
    private void processUserInput(String input) {
        try {
            int score = Integer.parseInt(input);
            tvResult.setText("スコア: " + score);
        } catch (NumberFormatException e) {
            tvResult.setText("エラー：数値ではない入力がありました");
        }
    }

    // --- 問題5：文字列比較 (修正済み) ---
    private void checkAdminStatus() {
        String currentRole = "ADMIN"; 
        if (currentRole.equals("ADMIN")) {
            tvResult.setText("管理者です");
        } else {
            tvResult.setText("拒否されました: [" + currentRole + "]");
        }
    }

    // --- 問題6：非同期処理 ---
    private void triggerAsyncDownload(final Button btn) {
        tvResult.setText("ダウンロード中...");
        btn.setEnabled(false);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                downloadCount++;
                runOnUiThread(() -> {
                    tvResult.setText("完了！合計: " + downloadCount);
                    btn.setEnabled(true);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() -> btn.setEnabled(true));
            }
        }).start();
    }

    // --- 問題7：リストの範囲外アクセス ---
    private void accessInvalidIndex() {
        List<String> fruits = new ArrayList<>(Arrays.asList("Apple", "Banana", "Cherry"));
        // 3つの要素（0, 1, 2）があるリストで、fruits.get(3) を呼ぼうとしてクラッシュします
        String lastFruit = fruits.get(fruits.size() -1); // 💥 sizeは3なので index 3 は存在しない
        tvResult.setText("最後のフルーツ: " + lastFruit);
    }

    // --- 問題8：小数計算の罠 ---
    private void checkFloatingPointMath() {
        double value = 0.0;
        for (int i = 0; i < 10; i++) {
            value += 0.1;
        }

        // 0.1を10回足すと 1.0 になるはずだが、doubleの精度誤差で 0.99999... になる
        if (Math.abs(value - 1.0) < 0.000001) {
            tvResult.setText("結果はピッタリ 1.0 です");
        } else {
            tvResult.setText("1.0 ではありません。実際は: " + value);
        }
    }


    // --- 問題9：無限再帰の修正案 ---
    private void startInfiniteRecursion(int count) {
        // 1. 終了条件（ここが重要！）
        if (count >= 100) {
            tvResult.setText("100回呼び出したので安全に停止しました");
            return; // ここでメソッドを抜ける
        }

        Log.d(TAG, "呼び出し回数: " + count);

        // 2. 次の呼び出し
        startInfiniteRecursion(count + 1);
    }
}
