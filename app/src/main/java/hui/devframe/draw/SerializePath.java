/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package hui.devframe.draw;

import android.graphics.Path;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lidongjun02 on 2015/5/8.
 */
public class SerializePath extends Path implements Serializable {

    private static final long serialVersionUID = -5974912367682897467L;

    public ArrayList<PathAction> actions = new ArrayList<PathAction>();

    @Override
    public void moveTo(float x, float y) {
        actions.add(new ActionMove(x, y));
        super.moveTo(x, y);
    }

    @Override
    public void quadTo(float x, float y, float x1, float y1) {
        actions.add(new ActionQuad(x, y, x1, y1));
        super.quadTo(x, y, x1, y1);
    }

    @Override
    public void lineTo(float x, float y) {
        actions.add(new ActionLine(x, y));
        super.lineTo(x, y);
    }

    public void drawThisPath() {
        for (PathAction p : actions) {
            if (p.getType().equals(PathAction.PathActionType.MOVE_TO)) {
                super.moveTo(p.getX(), p.getY());
            } else if (p.getType().equals(PathAction.PathActionType.LINE_TO)) {
                super.lineTo(p.getX(), p.getY());
            } else if (p.getType().equals(PathAction.PathActionType.QUAD_TO)) {
                super.quadTo(p.getX(), p.getY(), p.getX1(), p.getY1());
            }
        }
    }
    public static  ActionQuad GetQuadToAction(float x, float y, float x1, float y1){
        return new ActionQuad(x, y, x1, y1);
    }

    public interface PathAction {
        enum PathActionType {LINE_TO, MOVE_TO, QUAD_TO}

        PathActionType getType();

        float getX();

        float getY();

        float getX1();

        float getY1();
    }

    public static class ActionMove implements PathAction, Serializable {
        private static final long serialVersionUID = -7198142191254133295L;

        private float x, y;

        public ActionMove(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public PathActionType getType() {
            return PathActionType.MOVE_TO;
        }

        @Override
        public float getX() {
            return x;
        }

        @Override
        public float getY() {
            return y;
        }

        @Override
        public float getX1() {
            return 0;
        }

        @Override
        public float getY1() {
            return 0;
        }

    }

    public static class ActionQuad implements PathAction, Serializable {
        private static final long serialVersionUID = -7198142191254133295L;

        private float x, y, x1, y1;

        public ActionQuad(float x, float y, float x1, float y1) {
            this.x = x;
            this.y = y;
            this.x1 = x1;
            this.y1 = y1;
        }

        @Override
        public PathActionType getType() {
            return PathActionType.QUAD_TO;
        }

        @Override
        public float getX() {
            return x;
        }

        @Override
        public float getY() {
            return y;
        }

        public float getX1() {
            return x1;
        }

        public float getY1() {
            return y1;
        }

    }

    public static class ActionLine implements PathAction, Serializable {
        private static final long serialVersionUID = 8307137961494172589L;

        private float x, y;

        public ActionLine(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public PathActionType getType() {
            return PathActionType.LINE_TO;
        }

        @Override
        public float getX() {
            return x;
        }

        @Override
        public float getY() {
            return y;
        }

        @Override
        public float getX1() {
            return 0;
        }

        @Override
        public float getY1() {
            return 0;
        }

    }
}
