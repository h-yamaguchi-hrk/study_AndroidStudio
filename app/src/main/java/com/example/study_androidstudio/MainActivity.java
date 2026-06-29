package com.example.study_androidstudio;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private EditText etInputMain;
    private EditText etInputSub;
    private static final String TAG = "DebugPractice";
    private int downloadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        etInputMain = findViewById(R.id.etInputMain);
        etInputSub = findViewById(R.id.etInputSub);

        // --- 問題1〜9 (既存) ---
        findViewById(R.id.btnProblem1).setOnClickListener(v -> {
            int result = calculateTotal(10, 20);
            tvResult.setText("計算結果: " + result);
        });

        findViewById(R.id.btnProblem2).setOnClickListener(v -> {
            int result = runLoopAndSum();
            tvResult.setText("ループ合計: " + result);
        });

        findViewById(R.id.btnProblem3).setOnClickListener(v -> {
            causeCrash();
        });

        findViewById(R.id.btnProblem4).setOnClickListener(v -> {
            String userInput = "123a"; 
            processUserInput(userInput);
        });

        findViewById(R.id.btnProblem5).setOnClickListener(v -> {
            checkAdminStatus();
        });

        findViewById(R.id.btnProblem6).setOnClickListener(v -> {
            triggerAsyncDownload((Button) v);
        });

        findViewById(R.id.btnProblem7).setOnClickListener(v -> {
            accessInvalidIndex();
        });

        findViewById(R.id.btnProblem8).setOnClickListener(v -> {
            checkFloatingPointMath();
        });

        findViewById(R.id.btnProblem9).setOnClickListener(v -> {
            startInfiniteRecursion(0);
        });

        // --- 問題10〜12 (新規) ---
        findViewById(R.id.btnProblem10).setOnClickListener(v -> {
            handleEmptyInput();
        });

        findViewById(R.id.btnProblem11).setOnClickListener(v -> {
            validateEmailFormat();
        });

        findViewById(R.id.btnProblem12).setOnClickListener(v -> {
            checkPasswordMatch();
        });
    }

    // --- 問題1〜9のメソッド (省略せずに維持) ---
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

    private void processUserInput(String input) {
        try {
            int score = Integer.parseInt(input);
            tvResult.setText("スコア: " + score);
        } catch (NumberFormatException e) {
            tvResult.setText("エラー：数値ではない入力がありました");
        }
    }

    private void checkAdminStatus() {
        String currentRole = "ADMIN"; 
        if (currentRole.equals("ADMIN")) {
            tvResult.setText("管理者です");
        } else {
            tvResult.setText("拒否されました: [" + currentRole + "]");
        }
    }

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

    private void accessInvalidIndex() {
        List<String> fruits = new ArrayList<>(Arrays.asList("Apple", "Banana", "Cherry"));
        String lastFruit = fruits.get(fruits.size() - 1);
        tvResult.setText("最後のフルーツ: " + lastFruit);
    }

    private void checkFloatingPointMath() {
        double value = 0.0;
        for (int i = 0; i < 10; i++) {
            value += 0.1;
        }
        if (Math.abs(value - 1.0) < 0.000001) {
            tvResult.setText("結果はピッタリ 1.0 です");
        } else {
            tvResult.setText("1.0 ではありません。実際は: " + value);
        }
    }

    private void startInfiniteRecursion(int count) {
        if (count >= 100) {
            tvResult.setText("100回呼び出したので安全に停止しました");
            return;
        }
        Log.d(TAG, "呼び出し回数: " + count);
        startInfiniteRecursion(count + 1);
    }

    // --- 問題10：未入力チェックのバグ ---
    private void handleEmptyInput() {
        // メイン入力欄からテキストを取得
        String name = etInputMain.getText().toString();
        
        // バグ：入力が空の場合を考慮せずに最初の文字にアクセスしようとしてクラッシュします
        // デバッグワーク：空入力のときに StringIndexOutOfBoundsException が発生することを確認せよ
        char firstChar = name.charAt(0); 
        tvResult.setText("あなたの名前の頭文字は: " + firstChar);
    }

    // --- 問題11：メール形式バリデーションの罠 ---
    private void validateEmailFormat() {
        String email = etInputMain.getText().toString();
        // 簡易的なメールチェック用正規表現
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        
        // バグ：条件式の論理が逆になっている、または正規表現が不完全？
        // デバッグワーク：正しいメールを入れても「不正」と出る理由を突き止めよ
        if (!Pattern.matches(emailPattern, email)) {
            tvResult.setText("有効なメールアドレスです");
        } else {
            tvResult.setText("不正な形式のメールアドレスです");
        }
    }

    // --- 問題12：パスワード一致判定のミス ---
    private void checkPasswordMatch() {
        String pass1 = etInputMain.getText().toString();
        String pass2 = etInputSub.getText().toString();

        // バグ：Javaの文字列比較でありがちなミス、または空白の考慮漏れ？
        // デバッグワーク：全く同じ文字を入れても「不一致」になる理由を調査せよ
        // (ヒント: etInputSub.getText().toString() に意図しない何かが混じっているかも？)
        if (pass1 == pass2) {
            tvResult.setText("パスワードが一致しました");
        } else {
            tvResult.setText("パスワードが一致しません");
        }
    }
}
