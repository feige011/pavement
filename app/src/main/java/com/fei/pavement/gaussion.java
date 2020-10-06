package com.fei.pavement;
import java.util.ArrayList;
import java.util.List;

//outData��v_data Ϊһ��double���͵����飬����Ϊ�̶�Ϊ400
class gaussion {
	public void show(double[] outData,double[] v_data) {
		double averagedata = average(outData);//����ƽ��ֵ����
		double deviationdata = deviation(outData);//���ñ�׼���
		List<String> list=new ArrayList<String>();//���ø÷��������������Ԫ��
		//��outData��v_data�е�Ԫ�ؽ���һϵ���жϣ�Ȼ��ͨ���жϵ�Ԫ�ص���ż�������list
		for (int i=0;i<401;i++) {
			if (Math.abs(outData[i]-averagedata)>v_data[i]/8*deviationdata){  
				if (Math.abs(outData[i])>v_data[i]*0.04) {
					if (v_data[i]>20) {
						String s = String.valueOf(i);//��int��ת��Ϊstring��
						list.add(s);
					}
				}
			}
		}
	}
	
	//����һ�������������ƽ��ֵ
	private double average(double[] outData) {
		double result = 0;
		for(int i = 0;i < outData.length; i++) {
			result += outData[i] / outData.length;
		}
		return result;
	}
	
	//����һ�������������׼��
	private double deviation(double[] outData) {
		double result = 0;
		for (int i = 0; i < outData.length; i++) {
            result = result + (Math.pow((outData[i] - average(outData)), 2));
        }
        result = Math.sqrt(result / (outData.length-1));
		return result;
	}
}