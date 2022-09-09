package tfsapps.seeart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public abstract class PaintParts {
    private int obj_scale = 0;  //オブジェクトの大きさ
    private int obj_shape = 0;  //オブジェクトの形
    private int obj_x = 0;      //オブジェクトのX座標
    private int obj_y = 0;      //オブジェクトのy座標

    /* 図形の形*/
    public final int CIRCLE_0 = 0;      //円
    public final int CIRCLE_1 = 1;      //円塗りつぶし
    public final int SQUARE_0 = 2;      //四角
    public final int SQUARE_1 = 3;      //四角塗りつぶし
    public final int TRIANGLE_0 = 4;    //三角
    public final int TRIANGLE_1 = 5;    //三角塗りつぶし
    public final int STAR = 6;    //星型

    /*
    *   type:   形
    *   color:  色
    *   stroke:
    * */
    public abstract void draw(Canvas canvas);
}
