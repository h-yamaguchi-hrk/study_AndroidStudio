package com.example.study_androidstudio;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
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

        // 問題4：数字以外が入力されたら落ちるバグ
        findViewById(R.id.btnProblem4).setOnClickListener(v -> {
            String userInput = "123a"; // ユーザーが間違って文字を混ぜて入力したと想定
            processUserInput(userInput);
        });

        // 問題5：文字は合っているはずなのに、なぜか条件をすり抜けるバグ
        findViewById(R.id.btnProblem5).setOnClickListener(v -> {
            checkAdminStatus();
        });

        // 問題6：ボタンを連打するとカウントがめちゃくちゃになるバグ
        findViewById(R.id.btnProblem6).setOnClickListener(v -> {
            triggerAsyncDownload();
        });
    }

    // --- 問題1のバグメソッド ---
    private int calculateTotal(int a, int b) {
        Log.d(TAG, "calculateTotalが呼び出されました。a=" + a + ", b=" + b);
        int wrongMultiplier = 10;
        // バグ：本来は (a + b) ですが、練習用に multiplier を掛けています
        int total = (a + b) * wrongMultiplier;
        return total;
    }

    // --- 問題2のバグメソッド ---
    private int runLoopAndSum() {
        int sum = 0;
        for (int i = 1; i <= 5; i++) {
            sum += i;
            // バグ：i == 3 のときだけ合計を2倍にする謎の処理
            if (i == 3) {
                sum *= 2;
            }
        }
        return sum;
    }

    // --- 問題3のバグメソッド ---
    private void causeCrash() {
        List<String> textList = null;
        // バグ：nullオブジェクトに対してアクセスしているためクラッシュする
        int size = textList.size();
        Log.d(TAG, "サイズは: " + size);
    }

    // --- 問題4：型変換のバグ（初級・データ検証） ---
    private void processUserInput(String input) {
        // 【デバッグワーク】文字列を数値に変換しようとしてクラッシュします。
        // Logcatで「NumberFormatException」が発生していることを確認してください。
        try {
            int score = Integer.parseInt(input); // 💥「123a」は数値に変換できないのでクラッシュ！
            tvResult.setText("スコアを登録しました: " + score);
        } catch (NumberFormatException e) {
            tvResult.setText("エラー：正しい値を入力してください");
        }
    }

    // --- 問題5：文字列比較のバグ（中級・文字コードの罠） ---
    private void checkAdminStatus() {
        // サーバーや入力フォームから取得したデータ（末尾に目に見えないスペースが入っている想定）
        String currentRole = "ADMIN ";

        // 【デバッグワーク】この行にブレークポイントを置き、変数「currentRole」を右クリック。
        // 「Evaluate Expression」を開き、変数の正確な文字数や中身を調査せよ。
        if (currentRole.equals("ADMIN")) {
            tvResult.setText("管理者画面へようこそ");
        } else {
            // 本来は管理者なのに、なぜかこっちに入ってしまうバグ
            tvResult.setText("エラー：管理者権限がありません（現在の権限: " + currentRole + "）");
        }
    }

    // --- 問題6：非同期処理のバグ（上級・マルチスレッド） ---
    private void triggerAsyncDownload() {
        tvResult.setText("ダウンロード開始...");

        // 模擬的な非同期処理（1秒後にカウントを増やすスレッドを起動）
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 1秒待つ処理
                downloadCount++;

                // 画面を更新する（Androidはメインスレッド以外から画面を触るとエラーになるためrunOnUiThreadを使用）
                runOnUiThread(() -> {
                    tvResult.setText("ダウンロード完了！合計: " + downloadCount + "回");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
