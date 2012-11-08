package VB;

import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;

public class VSoundGenerator {
	public static float SAMPLE_RATE = 44100f;
	SourceDataLine sdl;
	AudioFormat af;
	
	public VSoundGenerator(){
		try{
			af = new AudioFormat(SAMPLE_RATE,8,1,true,false);
			sdl = AudioSystem.getSourceDataLine(af);
			sdl.open(af);
			sdl.start();
		}
		catch(Exception e){		System.out.println(e);		}
	}
	
	public void writeSound(int hz, int msecs, double vol) throws LineUnavailableException {
		if (hz <= 0)
			throw new IllegalArgumentException("Frequency <= 0 hz");
		if (msecs <= 0)
			throw new IllegalArgumentException("Duration <= 0 msecs");
		if (vol > 1.0 || vol < 0.0)
			throw new IllegalArgumentException("Volume out of range 0.0 - 1.0");

		byte[] buf = new byte[(int)SAMPLE_RATE * msecs / 1000];
	
		
		for (int i=0; i<buf.length; i++) {
			double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
			buf[i] = (byte)(Math.sin(angle) * 127.0 * vol);
		}
		sdl.write(buf,0,buf.length);
	}
	
	public void close()	{sdl.close();}
	public void play()	{sdl.drain();}
	
	
	public static void soundTest() throws LineUnavailableException{
		VSoundGenerator vsg = new VSoundGenerator();
		for(double pow = 2.0; pow <= 5.0; pow+=0.5){	
			for(int fq = 400; fq < 1200; fq+=100){
				for(int k = 1; k <= 20; k++){
					double vol = getVol(k,pow);//getVol(1,pow);	
					System.out.println("Frequency: "+fq+"    Power: "+pow+"   Volume: "+vol);					
					vsg.writeSound(fq,100,vol);
				}
			}
		}
		vsg.play();
		vsg.close();
	}

	public static void main(String[] args) throws LineUnavailableException {
	//	soundTest();
		
	//	simpleSong();
	
		JOptionPane.showMessageDialog(null, "Happy Birthday Victor!");
		JOptionPane.showMessageDialog(null, "(Make sure your speakers / headphones are on...)");
		playBDaySong();
		JOptionPane.showMessageDialog(null, "From Feni (Duh!)");
	}
	
	public static void playFreq(int freq) throws LineUnavailableException{
		playFreq(freq, .1);
	}
	
	public static void playFreq(int freq, double time) throws LineUnavailableException{	
		VSoundGenerator vsg = new VSoundGenerator();
		int msecs = (int) (time * 1000);
		for(int k = 1; k <= 20; k++){
			double vol = getVol(k,Math.E);
			vsg.writeSound(freq,msecs,vol);
		}
		vsg.play();
		vsg.close();
	}
	
	public static void playFile(String fileName) throws LineUnavailableException{
		try{
			Scanner input = new Scanner(new File(fileName));
			boolean firstLine = true;
			
			Song s = new Song();
			while(input.hasNext()){
				String str = input.nextLine();
				if(firstLine){
					Integer bpmIntVal = Integer.parseInt(str);
					s.setBPM(bpmIntVal.shortValue() );
					firstLine = false;
				}
				else{
					StringTokenizer st = new StringTokenizer(str,"|");
					Integer ocInt = Integer.parseInt(st.nextToken());
					byte octave = ocInt.byteValue();
					
					String note = st.nextToken();
				}
			}
		}
		catch(Exception e){		System.out.println(e);		}
	}
	
	public static void playTunes(String str) throws LineUnavailableException{
		StringTokenizer st = new StringTokenizer(str);
		while(st.hasMoreTokens()){
			new Player(Integer.parseInt(st.nextToken()));
			sleep(300);
		}
	}
	
	public static void sleep(int duration){
		try{	Thread.sleep(duration);	}
		catch(Exception e){		System.out.println(e);	}
	}
	
	static Random rand = new Random();
	public static double getVol(double x, double pow){
		double vol = (double)((double) 5*Math.log(x+1))/Math.pow(x+1,pow);

/*
 // Warning: This can cause errors in some special cases... Unlikely, but possible...
		double randomJerk = rand.nextDouble()/7;
		vol = vol+(vol*randomJerk);
*/
		return vol;
	}
	
	public static void simpleSong(){
		try{
			byte n = 0;
			playNote(n,"C");
			playNote(n,"D");
			playNote(n,"E");
			playNote(n,"B");
			playNote(n,"A");
			playNote(n,"C");
			playNote(n,"D");
			playNote(n,"E");
			playNote(n,"G");
			playNote(n,"F");
			playNote(n,"C");
			playNote(n,"D");
			playNote(n,"C");
			playNote(n,"D");
			playNote(n,"E");
			playNote(n,"B");
			playNote(n,"A");
			playNote(n,"C");
			playNote(n,"D");
			playNote(n,"E");
			playNote(n,"G");
			playNote(n,"F");
			playNote(n,"C");
			playNote(n,"D");
			playNote(n,"E");
			}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public static void playBDaySong(){
		try{
			byte n = 0;
			n = 0;
			
			playNote(n,"R");
			playNote(n,"R");
			
			playNote(n,"C");
			playNote(n,"C");
			playNote(n,"D");
			playNote(n,"C");
			playNote(n,"F");
			playNote(n,"E");
			
			playNote(n,"R");
			
			playNote(n,"C");
			playNote(n,"C");
			playNote(n,"D");
			playNote(n,"C");
			playNote(n,"G");
			playNote(n,"F");
			
			playNote(n,"R");
			
			playNote(n,"C");
			playNote(n,"C");
			n = 1;
			playNote(n,"C");
			n = 0;
			playNote(n,"A");
			playNote(n,"F");
			playNote(n,"E");
			playNote(n,"D");
			
			playNote(n,"R");
			
			playNote(n,"A");
			playNote(n,"A");
			playNote(n,"A");
			playNote(n,"F");
			playNote(n,"G");
			playNote(n,"F");
		}
		catch(Exception e){
			System.out.println(e);
		}		
	}
	
			
	public static void playNote(byte octaveDistance, String noteName/*, short unit, int */)  throws LineUnavailableException{
		int freq = (int) (getFreq2(noteName) * Math.pow(2,octaveDistance) );
		double length = 1 * 1;					//should be unit * measure(?)
		double time = ((double) 60 / 60 * length);		//should be 60 / BPM * length
		//playFreq(freq, time);
		new Player(freq,time);
		sleep(700);
	}
	
	private static int getFreq(String noteName){
	
		if (noteName.equals("C"))	{return 523;}
		if (noteName.equals("C#") ||
		    noteName.equals("Db"))	{return 554;}
		if (noteName.equals("D"))	{return 587;}
		if (noteName.equals("D#") ||
		    noteName.equals("Eb"))	{return 622;}
		if (noteName.equals("E"))	{return 659;}
		if (noteName.equals("F"))	{return 698;}
		if (noteName.equals("F#") ||
		    noteName.equals("Gb"))	{return 740;}
		if (noteName.equals("G"))	{return 784;}
		if (noteName.equals("G#") ||
		    noteName.equals("Ab"))	{return 831;}
		if (noteName.equals("A"))	{return 880;}
		if (noteName.equals("A#") ||
		    noteName.equals("Bb"))	{return 932;}
		if (noteName.equals("B"))	{return 988;}
		return 0;
	}


	private static int getFreq2(String noteName){
	
		if (noteName.equals("C"))	{return 520;}
		if (noteName.equals("C#") ||
		    noteName.equals("Db"))	{return 550;}
		if (noteName.equals("D"))	{return 590;}
		if (noteName.equals("D#") ||
		    noteName.equals("Eb"))	{return 620;}
		if (noteName.equals("E"))	{return 660;}
		if (noteName.equals("F"))	{return 700;}
		if (noteName.equals("F#") ||
		    noteName.equals("Gb"))	{return 740;}
		if (noteName.equals("G"))	{return 780;}
		if (noteName.equals("G#") ||
		    noteName.equals("Ab"))	{return 830;}
		if (noteName.equals("A"))	{return 880;}
		if (noteName.equals("A#") ||
		    noteName.equals("Bb"))	{return 930;}
		if (noteName.equals("B"))	{return 990;}
		return 0;
	}
}

class Song implements Runnable{
	short BPM = 0;
	
	public Song(){
		
	}
	
	public void setBPM(short bpmval){
		BPM = bpmval;
	}
	
	public void run(){
		
	}
}

class Player implements Runnable{
	int freq = 0;
	
	double time = 0.0;
	public Player(int i){
		freq = i;
		new Thread(this).start();
	}
	
	public Player(int i, double t){
		time = t;
		freq = i;
		new Thread(this).start();
	}
	
	public void run(){
		try{
			playFreq(freq);	
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void playFreq(int freq) throws LineUnavailableException{	
		playFreq(freq, .1);
	}
	
	public void playFreq(int freq, double time) throws LineUnavailableException{	
		VSoundGenerator vsg = new VSoundGenerator();
		int msecs = (int) (time * 1000);
		
		int rate = 1;
		for(int k = 1; k <= 30; k+=rate){
			double vol = VSoundGenerator.getVol(k,Math.E);
			if(vol < 0.09){
				rate*=2;
			}
			vsg.writeSound(freq,100,vol);
		}
		vsg.play();
		vsg.close();
	}
	
	public static void playTune(int i){
		try{
			int fq = 44300+(i*100);		// 440 = A on a piano		// 44100 default for the new sample rate
			VSoundGenerator vsg = new VSoundGenerator();
			for(int k = 1; k <= 20; k++){
				double vol = VSoundGenerator.getVol(k,2);				
				//vsg.writeSound(fq,60,vol);
				
				vsg.writeSound(fq-(k*100),60,vol);	//	Weird sound effect...
			}
			vsg.play();
			vsg.close();						
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
}