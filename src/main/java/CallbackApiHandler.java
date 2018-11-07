import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.objects.messages.Message;

public class CallbackApiHandler extends CallbackApi {
    @Override
    public void messageNew(Integer groupId, Message message) {
        System.out.println(message.getBody());
    }

}
