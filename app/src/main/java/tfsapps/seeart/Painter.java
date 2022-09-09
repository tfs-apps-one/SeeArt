package tfsapps.seeart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import androidx.annotation.NonNull;

public class Painter extends SurfaceView implements SurfaceHolder.Callback {
    Paint paint;
    Paint line_1;               //１行目テキスト
    Paint line_2;               //２行目テキスト
    Paint line_3;               //３行目テキスト
    Paint line_4;               //４行目テキスト
    private long time_count;    //ゲームタイマー
    private int game_level = 1; //ステージ（難易度レベル）
    private int star_num = 0;   //現在の星★の数
    private static final long FPS = 60;

    private final Random rand = new Random(System.currentTimeMillis());

    /* 生成した図形のキュー */
    private final List<PaintData> paintList = new ArrayList<PaintData>();

    private int game_status = 0;            //ゲームステータス管理
    private final int GAME_INITIAL = 0;     //ステージ初期状態
    private final int GAME_OPEING = 1;      //ステージの表示　中央に大きく
    private final int GAME_SETTING = 2;     //ゲーム準備中
    private final int GAME_SETEND = 3;     //ゲームプレイ中
    private final int GAME_PLAYING = 4;     //ゲームプレイ中
    private final int GAME_ENDING = 5;      //ゲーム終了中

    private int game_app_rate_obj;          //図形生成の出現率
    private int game_app_rate_star;         //星　生成の出現率
    private int game_scale;                 //図形の大きさ

    public Painter(Context context) {
        super(context);
        getHolder().addCallback(this);
        line_1 = new Paint();
        line_2 = new Paint();
        line_3 = new Paint();
        line_4 = new Paint();
    }

    /* ゲームバランス調整 */
    public void setGameBalance() {
        switch (game_level){
            case 1: //レベル１  初級
                game_app_rate_obj = 10;
                game_app_rate_star = 10;
                game_scale = getWidth()/10;
                break;
            case 2: //レベル２
                game_app_rate_obj = 3;
                game_app_rate_star = 5;
                game_scale = getWidth()/12;
                break;

            case 3: //レベル３
                game_app_rate_obj = 3;
                game_app_rate_star = 5;
                game_scale = getWidth()/15;
                break;

            case 4: //レベル４  中級
                game_app_rate_obj = 2;
                game_app_rate_star = 4;
                game_scale = getWidth()/15;
                break;

            case 5: //レベル５
                game_app_rate_obj = 2;
                game_app_rate_star = 4;
                game_scale = getWidth()/15;
                break;

            case 6: //レベル６
                game_app_rate_obj = 2;
                game_app_rate_star = 4;
                game_scale = getWidth()/20;
                break;

            case 7: //レベル７  上級
                game_app_rate_obj = 1;
                game_app_rate_star = 3;
                game_scale = getWidth()/20;
                break;

            case 8: //レベル８
                game_app_rate_obj = 1;
                game_app_rate_star = 3;
                game_scale = getWidth()/30;
                break;

            case 9: //レベル９
                game_app_rate_obj = 1;
                game_app_rate_star = 3;
                game_scale = getWidth()/30;
                break;

            case 10: //レベル１０ 神級
                game_app_rate_obj = 1;
                game_app_rate_star = 2;
                game_scale = getWidth()/30;
                break;

            default:
                game_app_rate_obj = 1;
                game_app_rate_star = 1;
                game_scale = getWidth()/30;
                break;
        }

        /* 準備中の時は、星★の出現率を高める */
        if (game_status == GAME_SETTING) {
            game_app_rate_star = 2;
        }
    }

    /********************************************************************************
     図形生成の処理
     *********************************************************************************/
    public void createObject(Canvas canvas){
        float xc = getWidth();
        float yc = getHeight();
        int type;
        int i;


        /* ゲームバランス調整 */
        setGameBalance();

        /* 図形の出現率 */
        type = rand.nextInt(1000);
        type = type % 7;

        if (type > 0 && type <= 5) {
            if ((time_count % game_app_rate_obj) != 0) {
                return;
            }
        }
        if (type == 6) {
            if ((time_count % game_app_rate_star) != 0) {
                return;
            }
        }

        paint = new Paint();
        /* オブジェクトの座標位置（X、Y） */
        float x = rand.nextInt((int) xc);
        float y;
        while(true){
             y = rand.nextInt((int) yc);
             if (y <= 150 || y >= (getHeight()-150)){
                 continue;
             }
             else{
                 break;
            }
        }

        /* オブジェクトの色指定 */
        int color_1 = rand.nextInt(255);
        int color_2 = rand.nextInt(255);
        int color_3 = rand.nextInt(255);
        int color_4 = rand.nextInt(255);

        int dp = rand.nextInt(3);

        /* 線の太さ */
        int stroke = rand.nextInt(15);
        if (stroke <=5) stroke=5;

        /* 図形の大きさ */
        int scale = rand.nextInt(game_scale);

        if (type == 6 && scale < 60){
            scale = 60;
        }

        PaintData paintData = new PaintData(paint,xc,yc,x,y,dp);
        paintData.PaintDataSetParam(color_1,color_2,color_3,color_4,scale,stroke,type);

        paintList.add(paintData);
    }

    /********************************************************************************
     描画処理
     *********************************************************************************/
    protected void drawTextLine(Canvas canvas) {
        float xc = getWidth();
        int font1 = (int)xc*4/100;
        int font2 = (int)xc*5/100;
        int font3 = (int)xc*7/100;

        String buff = "";
        int _index = game_level;
        if (game_level <= 3){
            buff += "初級 ";
        }else if(game_level <= 6){
            buff += "中級 ";
            _index -= 3;
        }else if(game_level <= 9){
            buff += "上級 ";
            _index -= 6;
        }else {
            buff += "神級 ";
            _index -= 9;
        }

        String level = "★";
        for(int i=0; i<_index; i++){
            buff += level;
        }

        switch (game_status){
            case GAME_INITIAL:
                break;

            case GAME_OPEING:
                line_1.setColor(Color.BLUE);
                line_1.setTextSize(font1);
                line_1.setTypeface(Typeface.DEFAULT_BOLD);
                line_1.setAntiAlias(true);
                canvas.drawText("星★を見つけてタッチしよう！！", 50, 60, line_1);

                line_2.setColor(Color.BLUE);
                line_2.setTextSize(font1);
                line_2.setTypeface(Typeface.DEFAULT_BOLD);
                line_2.setAntiAlias(true);
                canvas.drawText("全てタッチでステージクリア！！", 50, 120, line_2);

                line_3.setColor(Color.RED);
                line_3.setTextSize(font2);
                line_3.setTypeface(Typeface.DEFAULT_BOLD);
                line_3.setAntiAlias(true);
                canvas.drawText("レベル: " + buff, 50, 250, line_3);

                line_4.setColor(Color.BLACK);
                line_4.setTextSize(font3);
                line_4.setTypeface(Typeface.DEFAULT_BOLD);
                line_4.setAntiAlias(true);
                canvas.drawText("　ゲームを始めます", 50, 400, line_4);
                break;

            case GAME_SETTING:
                line_1.setColor(Color.BLUE);
                line_1.setTextSize(font1);
                line_1.setTypeface(Typeface.DEFAULT_BOLD);
                line_1.setAntiAlias(true);
                canvas.drawText("星★を見つけてタッチしよう！！", 50, 60, line_1);

                line_2.setColor(Color.BLUE);
                line_2.setTextSize(font1);
                line_2.setTypeface(Typeface.DEFAULT_BOLD);
                line_2.setAntiAlias(true);
                canvas.drawText("全てタッチでステージクリア！！", 50, 120, line_2);

                line_3.setColor(Color.RED);
                line_3.setTextSize(font2);
                line_3.setTypeface(Typeface.DEFAULT_BOLD);
                line_3.setAntiAlias(true);
                canvas.drawText("レベル: " + buff, 50, 250, line_3);

                line_4.setColor(Color.BLACK);
                line_4.setTextSize(font3);
                line_4.setTypeface(Typeface.DEFAULT_BOLD);
                line_4.setAntiAlias(true);
                canvas.drawText("　準備中...", 50, 400, line_4);
                break;

            case GAME_SETEND:
                line_1.setColor(Color.RED);
                line_1.setTextSize(font1);
                line_1.setTypeface(Typeface.DEFAULT_BOLD);
                line_1.setAntiAlias(true);
                canvas.drawText("レベル:" + buff, 50, 70, line_1);

                line_2.setColor(Color.RED);
                line_2.setTextSize(font1);
                line_2.setTypeface(Typeface.DEFAULT_BOLD);
                line_2.setAntiAlias(true);
                canvas.drawText("見つける星 残り:" + star_num + "個", 50, 140, line_1);

                line_3.setColor(Color.BLACK);
                line_3.setTextSize(font3);
                line_3.setTypeface(Typeface.DEFAULT_BOLD);
                line_3.setAntiAlias(true);
                canvas.drawText("　～スタート～ ", 50, 400, line_3);
                break;

            case GAME_PLAYING:
                line_1.setColor(Color.RED);
                line_1.setTextSize(font1);
                line_1.setTypeface(Typeface.DEFAULT_BOLD);
                line_1.setAntiAlias(true);
                canvas.drawText("レベル:" + buff, 50, 70, line_1);

                line_2.setColor(Color.RED);
                line_2.setTextSize(font1);
                line_2.setTypeface(Typeface.DEFAULT_BOLD);
                line_2.setAntiAlias(true);
                canvas.drawText("見つける星 残り:" + star_num + "個", 50, 140, line_1);
                break;

            case GAME_ENDING:
                line_1.setColor(Color.BLUE);
                line_1.setTextSize(font2);
                line_1.setTypeface(Typeface.DEFAULT_BOLD);
                line_1.setAntiAlias(true);
                canvas.drawText("おめでとう！！", 50, 100, line_1);

                line_2.setColor(Color.BLACK);
                line_2.setTextSize(font3);
                line_2.setTypeface(Typeface.DEFAULT_BOLD);
                line_2.setAntiAlias(true);
                canvas.drawText("☆☆☆☆☆☆☆☆☆☆", 50, 310, line_3);

                line_3.setColor(Color.BLACK);
                line_3.setTextSize(font3);
                line_3.setTypeface(Typeface.DEFAULT_BOLD);
                line_3.setAntiAlias(true);
                canvas.drawText("　ステージ　クリア　", 50, 400, line_3);

                line_4.setColor(Color.BLACK);
                line_4.setTextSize(font3);
                line_4.setTypeface(Typeface.DEFAULT_BOLD);
                line_4.setAntiAlias(true);
                canvas.drawText("★★★★★★★★★★", 50, 490, line_4);

                break;

        }

    }

    /********************************************************************************
        描画処理
    *********************************************************************************/
    protected void drawObject(Canvas canvas) {

        /* 背景 */
        canvas.drawColor(Color.argb(220, 255, 255, 255));   //白で影あり
//        canvas.drawColor(Color.argb(255, 255, 255, 255)); //白で影なし

        /* テキスト表示 */
        drawTextLine(canvas);

        switch (game_status){
            /* 初期状態 */
            case GAME_INITIAL:
                game_status = GAME_OPEING;
                return;     //  ★リターン
            /* オープニング　・・・ゲームスタート・・・ */
            case GAME_OPEING:
                if (time_count > 50){
                    game_status = GAME_SETTING;
                }
                return;     //  ★リターン
            /* ・・・準備中・・・ */
            case GAME_SETTING:
                if (time_count > 100 && star_num >= 3){
                    game_status = GAME_SETEND;
                    time_count = 200;
                }
                break;
            /*　・・・準備完了・・・ */
            case GAME_SETEND:
                if (time_count > 230){
                    game_status = GAME_PLAYING;
                }
                break;
            /* ゲーム中 */
            case GAME_PLAYING:
                /* 星がゼロ個になった場合 */
                if (star_num <= 0){
                    game_status = GAME_ENDING;
                    time_count = 0;
                }
                break;
            /* ステージクリア */
            case GAME_ENDING:
                /* オブジェクト全て消去 */
                for (int i = 0; i < paintList.size(); i++) {
                    PaintData object = paintList.get(i);
                    paintList.remove(object);
                }

                if (time_count > 70){
                    game_status = GAME_OPEING;
                    game_level++;
                    time_count = 0;
                }
                return;     //  ★リターン
        }

    /*******************************
     　以下、図形の表示・削除処理
     ******************************/

        /* 星★の数 */
        star_num = 0;

        /* 図形の生成 */
        createObject(canvas);

        /* 図形の表示 */
        for (int i = 0; i < paintList.size(); i++) {
            PaintData object = paintList.get(i);
            int s_x = rand.nextInt(100) + 1;    //  X軸の移動ベクトル（ +方向<50 -方向>50)
            int m_x = rand.nextInt(3);          //  X軸の移動量
            int s_y = rand.nextInt(100) + 1;    //  y軸の移動ベクトル（ +方向<50 -方向>50)
            int m_y = rand.nextInt(4);          //  X軸の移動量
            int s_s = rand.nextInt(100) + 1;    //  図形の大きさベクトル（ +方向<50 -方向>50)
            boolean scale_flag;                        //  大きさ変更フラグ（時間間隔によって決まる）

            /* 大きさを変更する間隔 */
            if ((time_count%60) == 0){
                scale_flag = true;
            }
            else{
                scale_flag = false;
            }
            /* 図形の移動 */
            object.move(s_x,m_x,s_y,m_y,s_s,scale_flag);
//            invalidate();
            /* 図形の表示 */
            object.draw(canvas);
        }

        /* 不要図形の消去 */
        for (int i = 0; i < paintList.size(); i++) {
            PaintData object = paintList.get(i);
            if (object.isObjAlive() == false){
                if(object.isObjHitPoint() == 0) {
                    paintList.remove(object);
                }
                else{
                    paintList.get(i).hitpoint--;
                }
            }
            else {
                if (object.isStar() == true && object.isObjAlive() == true){
                    star_num++;
                }
            }
        }
    }

    /********************************************************************************
        タッチイベント（タップ処理）
     *********************************************************************************/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.v("TAP>>>","xPos=" + event.getX() + ", yPos="+ event.getY());

                /* ゲームプレイ中のみ */
                if (game_status == GAME_PLAYING) {
                    for (int i = 0; i < paintList.size(); i++) {
                        PaintData object = paintList.get(i);
                        if (object.isObjHit(event.getX(), event.getY()) == true) {
                        }
                    }
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    /********************************************************************************
     ゲーム全体の描画間隔
     *********************************************************************************/
    private class DrawThread extends Thread {
        private boolean isFinished;
        @Override
        public void run() {
            super.run();
            time_count = 0;
            SurfaceHolder holder = getHolder();
            while (!isFinished) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    drawObject(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }

                try {
                    time_count++;
                    //オーバーフロー対策　最大値超えの初期化処理
                    if(time_count > 99999999){
                        time_count = 1;
                    }
                    sleep(1000 / FPS);
//                    sleep(10000 / FPS);
//                    sleep(10000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private DrawThread drawThread;

    public interface Callback {
    }

    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void startDrawThread() {
        stopDrawThread();
        drawThread = new DrawThread();
        drawThread.start();
    }

    public boolean stopDrawThread() {
        if (drawThread == null) {
            return false;
        }
        drawThread.isFinished = true;
        drawThread = null;
        return true;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        startDrawThread();  //描画スレッド起動
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
