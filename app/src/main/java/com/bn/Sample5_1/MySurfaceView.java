package com.bn.Sample5_1;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class MySurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;//�Ƕ����ű���
    private SceneRenderer mRenderer;//������Ⱦ��

	private float mPreviousY;//�ϴεĴ���λ��Y����
    private float mPreviousX;//�ϴεĴ���λ��X����

	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }

	//�����¼��ص�����
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();//��ȡ�˴δ��ص�y����
        float x = e.getX();//��ȡ�˴δ��ص�x����
        if (e.getAction() == MotionEvent.ACTION_MOVE) {//��Ϊ�ƶ�����
            float dy = y - mPreviousY;//���㴥��λ�õ�Yλ��
            float dx = x - mPreviousX;//���㴥��λ�õ�Xλ��
            for (SixPointedStar h : mRenderer.ha)//���ø�����������x�ᡢy����ת�ĽǶ�
            {
                h.yAngle += dx * TOUCH_SCALE_FACTOR;
                h.xAngle += dy * TOUCH_SCALE_FACTOR;
            }
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            performClick();

        }
        mPreviousY = y;//��¼���ر�y����
        mPreviousX = x;//��¼���ر�x����
//      ACTION_DOWN���뷵��true�����޷����������¼�
        return e.getAction() == MotionEvent.ACTION_DOWN;
    }

    @Override
    public boolean performClick() {
        return super.performClick();

    }

    private class SceneRenderer implements GLSurfaceView.Renderer {
        SixPointedStar[] ha = new SixPointedStar[6];//����������

        public void onDrawFrame(GL10 gl) {
            //�����Ȼ�������ɫ����
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //ѭ�����Ƹ���������
            for (SixPointedStar h : ha) {
            	h.drawSelf();
            }
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����ӿڵĴ�С��λ�� 
        	GLES30.glViewport(0, 0, width, height);
        	//�����ӿڵĿ�߱�
        	float ratio= (float) width / height;
            //��������ͶӰ
        	MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 10);

        	//���������
			MatrixState.setCamera(
					0, 0, 3f,
					0, 0, 0f,
					0f, 1.0f, 0.0f
					);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(1f,1f,1f, 1.0f);
            //���������������еĸ��������� 
            for(int i=0;i<ha.length;i++)
            {
            	ha[i]=new SixPointedStar(MySurfaceView.this,0.2f,0.5f,-0.3f*i);
            }
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        }
    }
}
