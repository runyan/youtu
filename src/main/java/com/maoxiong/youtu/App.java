package com.maoxiong.youtu;

import java.util.List;

import com.maoxiong.youtu.callback.CallBack;
import com.maoxiong.youtu.client.Client;
import com.maoxiong.youtu.entity.FaceItem;
import com.maoxiong.youtu.entity.InvoiceItem;
import com.maoxiong.youtu.entity.Item;
import com.maoxiong.youtu.entity.ItemCor;
import com.maoxiong.youtu.entity.request.impl.CreditCradDetectRequestEntity;
import com.maoxiong.youtu.entity.request.impl.FaceDectectRequestEntity;
import com.maoxiong.youtu.entity.request.impl.FoodDetectRequestEntity;
import com.maoxiong.youtu.entity.request.impl.IDDetectRequestEntity;
import com.maoxiong.youtu.entity.request.impl.InvoiceDetectRequestEntity;
import com.maoxiong.youtu.entity.request.impl.PlateDetectRequestEntity;
import com.maoxiong.youtu.entity.request.impl.TextToAudioRequestEntity;
import com.maoxiong.youtu.entity.result.BaseResult;
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
	
	private static final String QQ = "530913397";
	private static final String APP_ID = "10125863";
	private static final String SECRET_ID = "AKIDnV8bFlTMRnPxMbhtoZEhzCIGyZ2K5aTB";
	private static final String SECRET_KEY = "lLl613yk3kKxTp6LRXVJwPhonjopjT6b";
	
    public static void main( String[] args ) {
    	Initializer initializer = new Initializer.Builder()
    			.QQ(QQ)
    			.appId(APP_ID)
    			.secretId(SECRET_ID)
    			.secretKey(SECRET_KEY)
    			.bulid();
    	initializer.init();
    	DefaultRequestPool pool = new DefaultRequestPool();
    	Request faceDetectRequest = new FaceDetectRequest();
    	FaceDectectRequestEntity faceRequestEntity = new FaceDectectRequestEntity();
//    	faceRequestEntity.setFilePath("D://b.png");
    	faceRequestEntity.setFileUrl("https://pic4.zhimg.com/v2-334f7a126585e75a87c7a982cae77532_im.jpg");
    	faceDetectRequest.setParams(faceRequestEntity);
    	Client faceDetectClient = ClientFactory.constructClient(faceDetectRequest);
    	CallBack faceDetectCallBack = new CallBack() {

			@Override
			public void onFail(Exception e) {
				LogUtil.error(e.getMessage());
				e.printStackTrace();
			}

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, BaseResult result,
					Class<? extends BaseResult> responseClass) {
				LogUtil.info(isSuccess + " " + errorCode + " " + errorMsg);
				FaceDetectResult detectResult = (FaceDetectResult) result;
				List<FaceItem> faceList = detectResult.getFaceItemList();
				faceList.forEach(face -> {
					LogUtil.info("Gender: " + face.getGender());
					LogUtil.info("Beauty: " + face.getBeauty());
					LogUtil.info("Age: " + face.getAge());
				});
			}
    		
    	};
    	pool.addRequest(faceDetectClient, faceDetectCallBack);
    	Request foodDetectRequest = new FoodDetectRequest();
    	FoodDetectRequestEntity requestEntity = new FoodDetectRequestEntity();
    	requestEntity.setFileUrl("https://pic3.zhimg.com/80/v2-8352df032c855c3967467d4101c2fe6b_hd.jpg");
    	foodDetectRequest.setParams(requestEntity);
    	Client client = ClientFactory.constructClient(foodDetectRequest);
    	CallBack foodDetectCallBack = new CallBack() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String responseString, BaseResult responseEntity,
					Class<? extends BaseResult> responseClass) {
				FoodDetectResult result = (FoodDetectResult) responseEntity;
				LogUtil.info(result.isFood() + " " + result.getFoodConfidence() * 100);
			}

			@Override
			public void onFail(Exception e) {
				LogUtil.error(e.getMessage());
				e.printStackTrace();
			}
    		
    	};
    	pool.addRequest(client, foodDetectCallBack);
    	Request creditCardDetectRequest = new CreditCardDetectRequest();
    	CreditCradDetectRequestEntity entity = new CreditCradDetectRequestEntity();
    	entity.setFilePath("D://timg.jpg");
//    	entity.setFileUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528098767113&di=296907c60c43165203dc18db7c7c26b8&imgtype=0&src=http%3A%2F%2Fkameng.com%2Fuploadfile%2Fcard%2F64636055b42d4d73660faba5fb018c40.jpg");
    	creditCardDetectRequest.setParams(entity);
    	Client creditCardDetectClient = ClientFactory.constructClient(creditCardDetectRequest);
    	CallBack creditCardDetectCallback = new CallBack() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, BaseResult result,
					Class<? extends BaseResult> responseClass) {
				LogUtil.info(isSuccess + " " + errorCode + " " + errorMsg);
				CorDetectResult creditCardDetectResult = (CorDetectResult) result;
				List<Item> itemList = creditCardDetectResult.getItems();
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
    	pool.addRequest(creditCardDetectClient,  creditCardDetectCallback);
    	Request plateDetectRequest = new PlateDetectRequest();
    	PlateDetectRequestEntity plateDetectRequestEntity = new PlateDetectRequestEntity();
    	plateDetectRequestEntity.setFileUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528277425993&di=c08a42bd6f25f31c49c1cb34d247f1b3&imgtype=jpg&src=http%3A%2F%2Fimg3.imgtn.bdimg.com%2Fit%2Fu%3D1593700053%2C2642362344%26fm%3D214%26gp%3D0.jpg");
    	plateDetectRequest.setParams(plateDetectRequestEntity);
    	Client plateDetectClient = ClientFactory.constructClient(plateDetectRequest);
    	CallBack plateDetectCallback = new CallBack() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, BaseResult result,
					Class<? extends BaseResult> responseClass) {
				LogUtil.info(isSuccess + " " + errorCode + " " + errorMsg);
				CorDetectResult creditCardDetectResult = (CorDetectResult) result;
				List<Item> itemList = creditCardDetectResult.getItems();
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
    	pool.addRequest(plateDetectClient, plateDetectCallback);
    	Request iDDetectRequest = new IDDetectRequest();
    	IDDetectRequestEntity iDDetectEntity = new IDDetectRequestEntity(CardType.BACK, true);
    	iDDetectEntity.setFileUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528361074011&di=3f24d7f66344f3b09b65dfbba112873e&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dpixel_huitu%252C0%252C0%252C294%252C40%2Fsign%3Db05d0b3c38fa828bc52e95a394672458%2Fd788d43f8794a4c2717d681205f41bd5ad6e39a8.jpg");
//    	iDDetectEntity.setFileUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528361604248&di=7b0023003f834c7a75b2ece009d680ea&imgtype=jpg&src=http%3A%2F%2Fimg2.imgtn.bdimg.com%2Fit%2Fu%3D30879982%2C1711011546%26fm%3D214%26gp%3D0.jpg");
    	iDDetectRequest.setParams(iDDetectEntity);
    	Client iDDetectClient = ClientFactory.constructClient(iDDetectRequest);
    	CallBack iDDetectCallback = new CallBack() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, BaseResult result,
					Class<? extends BaseResult> responseClass) {
				IDDetectResult idCard = (IDDetectResult) result;
				LogUtil.info("authority: " + idCard.getAuthority());
				LogUtil.info("validDate: " + idCard.getValidDate());
				LogUtil.info("id: " + idCard.getId());
				LogUtil.info("name: " + idCard.getName());
				LogUtil.info("address: " + idCard.getAddress());
				LogUtil.info("sex: " + idCard.getSex());
				LogUtil.info("nation: " + idCard.getNation());
				LogUtil.info("birth: " + idCard.getBirth());
				LogUtil.info("watermaskStatus: " + idCard.getWatermaskStatus());
			}

			@Override
			public void onFail(Exception e) {
				LogUtil.error(e.getMessage());
				e.printStackTrace();
			}
    		
    	};
    	pool.addRequest(iDDetectClient, iDDetectCallback);
    	Request text2AudioRequest = new TextToAudioRequest(true);
    	TextToAudioRequestEntity text2AudioRequestEntity = new TextToAudioRequestEntity();
    	text2AudioRequestEntity.setModelType(ModelType.FEMALE);
    	text2AudioRequestEntity.setSpeed(VoiceSpeed.NORMAL);
    	text2AudioRequestEntity.setText("腾讯优图，让未来在你身边");
    	text2AudioRequest.setParams(text2AudioRequestEntity);
    	Client textToAudioClient = ClientFactory.constructClient(text2AudioRequest);
    	CallBack textToAudioCallback = new CallBack() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, BaseResult result,
					Class<? extends BaseResult> responseClass) {
				TextToAudioResult voice = (TextToAudioResult) result;
				LogUtil.info("voice: " + voice.getVoice());
			}

			@Override
			public void onFail(Exception e) {
				LogUtil.error(e.getMessage());
				e.printStackTrace();
			}
    		
    	};
    	Request invoiceDetectRequest = new InvoiceDetectRequest();
    	InvoiceDetectRequestEntity invoiceDetectRequestEntity = new InvoiceDetectRequestEntity();
    	invoiceDetectRequestEntity.setFileUrl("D://invoice.jpg");
    	invoiceDetectRequest.setParams(invoiceDetectRequestEntity);
    	Client invoiceDetectClient = ClientFactory.constructClient(invoiceDetectRequest);
    	CallBack invoiceDetectCallback = new CallBack() {

			@Override
			public void onSuccess(boolean isSuccess, String errorCode, String errorMsg, BaseResult result,
					Class<? extends BaseResult> responseClass) {
				InvoiceDetectResult resultEntity = (InvoiceDetectResult) result;
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
    	Thread t1 = new Thread(() -> {
    		java.util.Map<Client, CallBack> map = new java.util.HashMap<>();
        	map.put(invoiceDetectClient, invoiceDetectCallback);
        	map.put(textToAudioClient, textToAudioCallback);
        	pool.addRequestsByMap(map);
    	});
    	t1.start();
    	try {
			t1.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	pool.execute();
//    	pool.close();
    }
    
}
