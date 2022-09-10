package tfsapps.seeart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class PaintData extends PaintParts {
    private float screen_x;
    private float screen_y;
    private float x;
    private float y;
    private int dp;

    private Paint paint;
    private int color1;
    private int color2;
    private int color3;
    private int color4;
    private int scale;
    private int stroke;
    private int type;

    private int sign_x = 0;
    private int sign_y = 0;
    private int move_x = 0;
    private int move_y = 0;

    private int sign_scale = 0;
    private boolean isAlive = false;
    public int hitpoint = 20;

    /* オブジェクトの生成（インスタンス） */
    public PaintData(Paint data,
                     float sc_x,
                     float sc_y,
                     float obj_x,
                     float obj_y,
                     int obj_dp) {
        super();
        paint = data;
        screen_x = sc_x;
        screen_y = sc_y;
        x = obj_x;
        y = obj_y;
        dp = obj_dp;
        isAlive = true;
    }

    /* スターのヒット判定 */
    public boolean isObjHit(float tap_x, float tap_y){

        float r = (float)scale;
        PointF center = new PointF(x, y);

        /* 星であるか？ */
        if (type != STAR){
            return false;
        }
        /* 星の場合 */
        else{
            /* ヒット判定ON */
            if ((tap_x > x-scale) && (tap_x < x + scale + scale) &&
                    (tap_y > y-scale-scale) && (tap_y < y +scale)){
                isAlive = false;
//                paint.setColor(Color.argb(0, 0, 0, 0));
                return true;
            }

        }
        return false;
    }

    /* オブジェクトが星であるか？ */
    public boolean isStar(){
        if(type == STAR)    return true;
        else                return false;
    }

    /* オブジェクトの生死 */
    public boolean isObjAlive(){
        return isAlive;
    }

    /* オブジェクトのヒットポイント */
    public int isObjHitPoint(){
        return hitpoint;
    }

    /* オブジェクトの設定値 */
    public void PaintDataSetParam(int c1,int c2,int c3,int c4, int sc, int st, int ty) {
        color1 = c1;
        color2 = c2;
        color3 = c3;
        color4 = c4;
        scale = sc;
        stroke = st;
        type = ty;
    }

    /* オブジェクトの移動 */
    public void move(int s_x, int mv_x, int s_y, int mv_y, int s_s, boolean s_flag){
        if (sign_x == 0)            sign_x = s_x;
        if (move_x == 0)            move_x = mv_x;
        if (sign_y == 0)            sign_y = s_y;
        if (move_y == 0)            move_y = mv_y;
        if (sign_scale == 0)        sign_scale = s_s;

        //  X軸の移動
        if (sign_x < 50)            x += mv_x;
        else                        x -= mv_x;
        //  y軸の移動
        if (sign_y < 50)            y += mv_y;
        else                        y -= mv_y;
        //  図形の大きさ
        if (s_flag == true){
            if (sign_scale < 50){
                scale++;
            }
            else{
                scale--;
                if(scale < 3)  scale = 3;
            }
        }

        /* 画面範囲外 */
        if (x < -10 ){
            isAlive = false;    hitpoint = 0;
        }
        if (x > screen_x + 10){
            isAlive = false;    hitpoint = 0;
        }
//        if (y < -10)                isAlive = false;
        if (y < 0){
//        if (y < 150){
                isAlive = false;    hitpoint = 0;
        }
//        if (y > screen_y + 10){
        if (y > (screen_y - 50)){
//        if (y > (screen_y - 150)){
                isAlive = false;    hitpoint = 0;
        }

    }

    @Override
    public void draw(Canvas canvas) {
        /* ここでタイプ表示？？ */
        switch (type) {
            // 円
            case CIRCLE_0:
                paint.setColor(Color.argb(color1, color2, color3, color4));
                paint.setStrokeWidth(stroke);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);
                // (x1,y1,r,paint) 中心x1座標, 中心y1座標, r半径
                canvas.drawCircle(x - dp, y - dp, scale, paint);
//                canvas.drawCircle(x - 15 * dp, y - 55 * dp, scale, paint);
                break;
            // 円    塗りつぶし
            case CIRCLE_1:
                paint.setColor(Color.argb(color1, color2, color3, color4));
                paint.setAntiAlias(true);
//                paint.setStrokeWidth(0);
//                paint.setStyle(Paint.Style.STROKE);
                // (x1,y1,r,paint) 中心x1座標, 中心y1座標, r半径
                canvas.drawCircle(x - dp, y - dp, scale, paint);
//                canvas.drawCircle(x - 15 * dp, y - 55 * dp, scale, paint);
                break;

            // 矩形
            case SQUARE_0:
                // 矩形
                paint.setColor(Color.argb(color1, color2, color3, color4));
                paint.setStrokeWidth(stroke);
                paint.setStyle(Paint.Style.STROKE);
                // (x1,y1,x2,y2,paint) 左上の座標(x1,y1), 右下の座標(x2,y2)
                canvas.drawRect(x - scale, y - scale,
                        x + scale * dp, y + scale * dp, paint);

/*                canvas.drawRect(x - 30 * dp, y - 50 * dp,
                        x + 120 * dp, y + 100 * dp, paint);*/
                break;

            // 矩形    塗りつぶし
            case SQUARE_1:
                // 矩形
                paint.setColor(Color.argb(color1, color2, color3, color4));
                // (x1,y1,x2,y2,paint) 左上の座標(x1,y1), 右下の座標(x2,y2)
                canvas.drawRect(x - scale * dp, y - scale * dp,
                        x + scale * dp, y + scale * dp, paint);
/*                canvas.drawRect(x - 30 * dp, y - 50 * dp,
                        x + 120 * dp, y + 100 * dp, paint);*/
                break;

            //  三角
            case TRIANGLE_0:
                Path path3 = new Path();
                paint.setColor(Color.argb(color1, color2, color3, color4));
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(stroke);
                int width2 = (3*scale);
                path3.moveTo(x+width2, y+(width2/3));
                path3.lineTo(x+width2-(width2/3),y+width2);
                path3.lineTo(x+width2+(width2/3),y+width2);
                path3.close();
                canvas.drawPath(path3,paint);
                break;

            //  三角      塗りつぶし
            case TRIANGLE_1:
                Path path2 = new Path();
                paint.setColor(Color.argb(color1, color2, color3, color4));
                paint.setStyle(Paint.Style.FILL);
                int width = (3*scale);
                path2.moveTo(x+width, y+(width/3));
                path2.lineTo(x+width-(width/3),y+width);
                path2.lineTo(x+width+(width/3),y+width);
                canvas.drawPath(path2,paint);
                break;

            //  星型
            case STAR:
                Path path = new Path();
                if(isAlive == true) {
                    if (color1 < 150)   color1 = 170;
                    paint.setColor(Color.argb(color1, color2, color3, color4));
                }
                else{
                    if ((hitpoint % 2) ==0) {
                        paint.setColor(Color.argb(255, 240, 240, 0));
                    }
                    else if((hitpoint % 2) ==0){
                        paint.setColor(Color.argb(200, 200, 200, 0));
                    }
                    else {
                        paint.setColor(Color.argb(160, 160, 160, 0));
                    }
                }
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                path.reset();
                float theta = (float)(Math.PI * 72 / 180);
                float r = (float)scale;
                PointF center = new PointF(x, y);
                float dx1 = (float)(r*Math.sin(theta));
                float dx2 = (float)(r*Math.sin(2*theta));
                float dy1 = (float)(r*Math.cos(theta));
                float dy2 = (float)(r*Math.cos(2*theta));
                path.moveTo(center.x, center.y-r);
                path.lineTo(center.x-dx2, center.y-dy2);
                path.lineTo(center.x+dx1, center.y-dy1);
                path.lineTo(center.x-dx1, center.y-dy1);
                path.lineTo(center.x+dx2, center.y-dy2);
                path.lineTo(center.x, center.y-r);
                canvas.drawPath(path, paint);
                break;
        }
    }
}
