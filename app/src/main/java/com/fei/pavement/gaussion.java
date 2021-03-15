package com.fei.pavement;
import java.util.ArrayList;
import java.util.List;

//outData��v_data Ϊһ��double���͵����飬����Ϊ�̶�Ϊ400
class gaussion {
	public List<Integer> show(ArrayList<Double> outData, ArrayList<Double> v_data) {
		double averagedata = average(outData);//����ƽ��ֵ����
		double deviationdata = deviation(outData);//���ñ�׼���
//		System.out.println();
		List<Integer> list=new ArrayList<Integer>();//���ø÷��������������Ԫ��
		//��outData��v_data�е�Ԫ�ؽ���һϵ���жϣ�Ȼ��ͨ���жϵ�Ԫ�ص���ż�������list
		for (int i=0;i<12000;i++) {
			if (Math.abs(outData.get(i)-averagedata)>v_data.get(i)/8*deviationdata){
				if (Math.abs(outData.get(i))>v_data.get(i)*0.04) {
					if (v_data.get(i)>20) {
//						String s = String.valueOf(i);//��int��ת��Ϊstring��
//						System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
						list.add(i);
					}
				}
			}
		}
		return list;
	}

	//����һ�������������ƽ��ֵ
	private double average(ArrayList<Double> outData) {
		double result = 0;
		for(int i = 0;i < 12000; i++) {
			result += outData.get(i) / 12000;
		}
		return result;
	}

	//����һ�������������׼��
	private double deviation(ArrayList<Double> outData) {
		double result = 0;
		for (int i = 0; i < 12000; i++) {
			result = result + (Math.pow((outData.get(i) - average(outData)), 2));
		}
		result = Math.sqrt(result / (12000-1));
		return result;
	}
}