/**
 * Created by Philipp Polland on 21-May-20.
 */

package util;

public class clsVectorMaths {

    public static float[] getMidPoint( float x1, float y1, float x2, float y2 ){
        float[] v1 = new float[]{x1,y1};
        float[] v2 = new float[]{x2,y2};
        float[] vDelta = new float[]{x2-x1,y2-y1};

        vDelta = normalize(vDelta);
        vDelta[0] *= vDelta[2]/2;
        vDelta[1] *= vDelta[2]/2;
        vDelta[2] = vDelta[2]/2;
        return vDelta;
    }



    public static float[] normalize(float[] vec){
        float len = (float) Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1]);
        vec[0] /= len;
        vec[1] /= len;
        return new float[]{vec[0],vec[1],len};
    }

}
