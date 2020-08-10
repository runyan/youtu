package com.maoxiong.youtu;

import java.util.List;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.entity.result.basic.FaceItem;
import com.maoxiong.youtu.entity.result.basic.InvoiceItem;
import com.maoxiong.youtu.entity.result.basic.Item;
import com.maoxiong.youtu.entity.result.basic.ItemCor;
import com.maoxiong.youtu.entity.request.impl.CreditCradDetectRequestEntity;
import com.maoxiong.youtu.entity.request.impl.FaceDectectRequestEntity;
import com.maoxiong.youtu.entity.request.impl.FoodDetectRequestEntity;
import com.maoxiong.youtu.entity.request.impl.IDDetectRequestEntity;
import com.maoxiong.youtu.entity.request.impl.InvoiceDetectRequestEntity;
import com.maoxiong.youtu.entity.request.impl.PlateDetectRequestEntity;
import com.maoxiong.youtu.entity.request.impl.TextToAudioRequestEntity;
import com.maoxiong.youtu.entity.result.impl.CorDetectResult;
import com.maoxiong.youtu.entity.result.impl.FaceDetectResult;
import com.maoxiong.youtu.entity.result.impl.FoodDetectResult;
import com.maoxiong.youtu.entity.result.impl.IDDetectResult;
import com.maoxiong.youtu.entity.result.impl.InvoiceDetectResult;
import com.maoxiong.youtu.entity.result.impl.TextToAudioResult;
import com.maoxiong.youtu.enums.CardType;
import com.maoxiong.youtu.enums.ModelType;
import com.maoxiong.youtu.enums.VoiceSpeed;
import com.maoxiong.youtu.factory.ClientFactory;
import com.maoxiong.youtu.initializer.Initializer;
import com.maoxiong.youtu.pool.impl.DefaultRequestPool;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.request.impl.CreditCardDetectRequest;
import com.maoxiong.youtu.request.impl.FaceDetectRequest;
import com.maoxiong.youtu.request.impl.FoodDetectRequest;
import com.maoxiong.youtu.request.impl.IDDetectRequest;
import com.maoxiong.youtu.request.impl.InvoiceDetectRequest;
import com.maoxiong.youtu.request.impl.PlateDetectRequest;
import com.maoxiong.youtu.request.impl.TextToAudioRequest;
import com.maoxiong.youtu.util.LogUtil;


/************************************************************
*                                                           *
*  .=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-.       *
*   |                     ______                     |      *
*   |                  .-"      "-.                  |      *
*   |                 /            \                 |      *
*   |     _          |              |          _     |      *
*   |    ( \         |,  .-.  .-.  ,|         / )    |      *
*   |     > "=._     | )(__/  \__)( |     _.=" <     |      *
*   |    (_/"=._"=._ |/     /\     \| _.="_.="\_)    |      *
*   |           "=._"(_     ^^     _)"_.="           |      *
*   |               "=\__|IIIIII|__/="               |      *
*   |              _.="| \IIIIII/ |"=._              |      *
*   |    _     _.="_.="\          /"=._"=._     _    |      *
*   |   ( \_.="_.="     `--------`     "=._"=._/ )   |      *
*   |    > _.="                            "=._ <    |      *
*   |   (_/                                    \_)   |      *
*   |                                                |      *
*   '-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='      *
*                                                           *
*      LASCIATE OGNI SPERANZA, VOI CH'ENTRATE               *
*      @author yanrun                                       *
*************************************************************/
public class App {
	
	private static final String QQ = "";
	private static final String APP_ID = "";
	private static final String SECRET_ID = "";
	private static final String SECRET_KEY = "";
	
	private static final DefaultRequestPool POOL = new DefaultRequestPool();
	
    public static void main( String... args ) {
    	init();
    	initFaceDetect();
    	initFoodDetect();
    	initCreditCardDetect();
    	initPlateDetect();
    	initIdDetect();
    	initText2Audio();
    	initInvoiceDetect();
    	POOL.execute();
//    	pool.close();
    }
    
    private static void init() {
    	Initializer initializer = new Initializer.Builder()
    			.qq(QQ)
    			.appId(APP_ID)
    			.secretId(SECRET_ID)
    			.secretKey(SECRET_KEY)
    			.bulid();
    	initializer.init();
    }
    
    private static void initInvoiceDetect() {
    	Request invoiceDetectRequest = new InvoiceDetectRequest();
    	InvoiceDetectRequestEntity invoiceDetectRequestEntity = new InvoiceDetectRequestEntity();
    	invoiceDetectRequestEntity.setFileUrl("D://invoice.jpg");
    	invoiceDetectRequest.setParams(invoiceDetectRequestEntity);
    	Client invoiceDetectClient = ClientFactory.constructClient(invoiceDetectRequest);
    	CallBack<InvoiceDetectResult> invoiceDetectCallback = new CallBack<InvoiceDetectResult>() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, InvoiceDetectResult resultEntity) {
				List<InvoiceItem>  itemList = resultEntity.getItems();
				itemList.forEach(item -> {
					LogUtil.info("item:" + item);
				});
			}

			@Override
			public void onFail(Exception e) {
				LogUtil.error(e.getMessage());
				e.printStackTrace();
			}

    	};
    	POOL.addRequest(invoiceDetectClient, invoiceDetectCallback);
    }
    
    private static void initText2Audio() {
    	Request text2AudioRequest = new TextToAudioRequest(true);
    	TextToAudioRequestEntity text2AudioRequestEntity = new TextToAudioRequestEntity();
    	text2AudioRequestEntity.setModelType(ModelType.FEMALE);
    	text2AudioRequestEntity.setSpeed(VoiceSpeed.NORMAL);
    	text2AudioRequestEntity.setText("腾讯优图，让未来在你身边");
    	text2AudioRequest.setParams(text2AudioRequestEntity);
    	Client textToAudioClient = ClientFactory.constructClient(text2AudioRequest);
    	CallBack<TextToAudioResult> textToAudioCallback = new CallBack<TextToAudioResult>() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, TextToAudioResult result) {
				TextToAudioResult voice = (TextToAudioResult) result;
				LogUtil.info("voice: " + voice.getVoice());
			}

			@Override
			public void onFail(Exception e) {
				LogUtil.error(e.getMessage());
				e.printStackTrace();
			}
    		
    	};
    	POOL.addRequest(textToAudioClient, textToAudioCallback);
    }
    
    private static void initIdDetect() {
    	Request idDetectRequest = new IDDetectRequest();
    	IDDetectRequestEntity idDetectEntity = new IDDetectRequestEntity(CardType.BACK, true);
    	idDetectEntity.setFileUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528361074011&di=3f24d7f66344f3b09b65dfbba112873e&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dpixel_huitu%252C0%252C0%252C294%252C40%2Fsign%3Db05d0b3c38fa828bc52e95a394672458%2Fd788d43f8794a4c2717d681205f41bd5ad6e39a8.jpg");
//    	iDDetectEntity.setFileUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528361604248&di=7b0023003f834c7a75b2ece009d680ea&imgtype=jpg&src=http%3A%2F%2Fimg2.imgtn.bdimg.com%2Fit%2Fu%3D30879982%2C1711011546%26fm%3D214%26gp%3D0.jpg");
    	idDetectRequest.setParams(idDetectEntity);
    	Client idDetectClient = ClientFactory.constructClient(idDetectRequest);
    	CallBack<IDDetectResult> idDetectCallback = new CallBack<IDDetectResult>() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, IDDetectResult result) {
				LogUtil.info("authority: " + result.getAuthority());
				LogUtil.info("validDate: " + result.getValidDate());
				LogUtil.info("id: " + result.getId());
				LogUtil.info("name: " + result.getName());
				LogUtil.info("address: " + result.getAddress());
				LogUtil.info("sex: " + result.getSex());
				LogUtil.info("nation: " + result.getNation());
				LogUtil.info("birth: " + result.getBirth());
				LogUtil.info("watermaskStatus: " + result.getWatermaskStatus());
			}

			@Override
			public void onFail(Exception e) {
				LogUtil.error(e.getMessage());
				e.printStackTrace();
			}
    		
    	};
    	POOL.addRequest(idDetectClient, idDetectCallback);
    }
    
    private static void initPlateDetect() {
    	Request plateDetectRequest = new PlateDetectRequest();
    	PlateDetectRequestEntity plateDetectRequestEntity = new PlateDetectRequestEntity();
//    	plateDetectRequestEntity.setFileUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528277425993&di=c08a42bd6f25f31c49c1cb34d247f1b3&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D1593700053%2C2642362344%26fm%3D214%26gp%3D0.jpg");
    	plateDetectRequestEntity.setFilePath("D:\\plate.jpg");
    	plateDetectRequest.setParams(plateDetectRequestEntity);
    	Client plateDetectClient = ClientFactory.constructClient(plateDetectRequest);
    	CallBack<CorDetectResult> plateDetectCallback = new CallBack<CorDetectResult>() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, CorDetectResult result) {
				LogUtil.info(isSuccess + " " + errorCode + " " + errorMsg);
				List<Item> itemList = result.getItems();
				itemList.forEach(item -> {
					LogUtil.info("item: " + item.getItem());
					LogUtil.info("itemString: " + item.getItemString());
					LogUtil.info("itemConf: " + item.getItemConf() * 100);
					final ItemCor cor = item.getCor();
					LogUtil.info("itemCor x: " + cor.getX() + " " + "y: " + cor.getY() + " " + "height: " + cor.getHeight() + " " 
							+ "width: " + cor.getWidth());
				});
			}

			@Override
			public void onFail(Exception e) {
				LogUtil.error(e.getMessage());
				e.printStackTrace();
			}
    		
    	};
    	POOL.addRequest(plateDetectClient, plateDetectCallback);
    }
    
    private static void initFaceDetect() {
    	Request faceDetectRequest = new FaceDetectRequest();
    	FaceDectectRequestEntity faceRequestEntity = new FaceDectectRequestEntity();
//    	faceRequestEntity.setFilePath("D://b.png");
    	faceRequestEntity.setFileUrl("https://pic4.zhimg.com/v2-334f7a126585e75a87c7a982cae77532_im.jpg");
    	faceDetectRequest.setParams(faceRequestEntity);
    	Client faceDetectClient = ClientFactory.constructClient(faceDetectRequest);
    	CallBack<FaceDetectResult> faceDetectCallBack = new CallBack<FaceDetectResult>() {

			@Override
			public void onFail(Exception e) {
				LogUtil.error(e.getMessage());
				e.printStackTrace();
			}

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, FaceDetectResult result) {
				LogUtil.info(isSuccess + " " + errorCode + " " + errorMsg);
				List<FaceItem> faceList = result.getFaceItemList();
				faceList.forEach(face -> {
					LogUtil.info("Gender: " + face.getGender());
					LogUtil.info("Beauty: " + face.getBeauty());
					LogUtil.info("Age: " + face.getAge());
				});
			}
    		
    	};
    	POOL.addRequest(faceDetectClient, faceDetectCallBack);
    }
    
    private static void initFoodDetect() {
    	Request foodDetectRequest = new FoodDetectRequest();
    	FoodDetectRequestEntity requestEntity = new FoodDetectRequestEntity();
    	requestEntity.setFileUrl("https://pic3.zhimg.com/80/v2-8352df032c855c3967467d4101c2fe6b_hd.jpg");
    	foodDetectRequest.setParams(requestEntity);
    	Client client = ClientFactory.constructClient(foodDetectRequest);
    	CallBack<FoodDetectResult> foodDetectCallBack = new CallBack<FoodDetectResult>() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String responseString, FoodDetectResult result) {
				LogUtil.info(result.isFood() + " " + result.getFoodConfidence() * 100);
			}

			@Override
			public void onFail(Exception e) {
				LogUtil.error(e.getMessage());
				e.printStackTrace();
			}
    		
    	};
    	POOL.addRequest(client, foodDetectCallBack);
    }
    
    private static void initCreditCardDetect() {
    	Request creditCardDetectRequest = new CreditCardDetectRequest();
    	CreditCradDetectRequestEntity entity = new CreditCradDetectRequestEntity();
    	entity.setFilePath("D://timg.jpg");
    	creditCardDetectRequest.setParams(entity);
    	Client creditCardDetectClient = ClientFactory.constructClient(creditCardDetectRequest);
    	CallBack<CorDetectResult> creditCardDetectCallback = new CallBack<CorDetectResult>() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, CorDetectResult result) {
				LogUtil.info(isSuccess + " " + errorCode + " " + errorMsg);
				List<Item> itemList = result.getItems();
				itemList.forEach(item -> {
					LogUtil.info("item: " + item.getItem());
					LogUtil.info("itemString: " + item.getItemString());
					LogUtil.info("itemConf: " + item.getItemConf() * 100);
					final ItemCor cor = item.getCor();
					LogUtil.info("itemCor x: " + cor.getX() + " " + "y: " + cor.getY() + " " + "height: " + cor.getHeight() + " " 
							+ "width: " + cor.getWidth());
				});
			}

			@Override
			public void onFail(Exception e) {
				LogUtil.error(e.getMessage());
				e.printStackTrace();
			}
    		
    	};
    	POOL.addRequest(creditCardDetectClient,  creditCardDetectCallback);
    }
}
