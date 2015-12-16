package id.merv.cdp.book;

import android.app.Application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.squareup.okhttp.OkHttpClient;

import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by akm on 18/11/15.
 */
public class MeruvianBookApplication extends Application {

    private static MeruvianBookApplication instance;

    private JobManager jobManager;
    private Retrofit retrofit;
    private ObjectMapper objectMapper;

    public MeruvianBookApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new FontAwesomeModule());

//        objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        configureJobManager();
//        configureRestAdaper();
    }

    private void configureRestAdaper() {
        OkHttpClient client = new OkHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://demo.merv.id")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }

    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        jobManager = new JobManager(this, configuration);
    }

    public static MeruvianBookApplication getInstance() {
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
    public JobManager getJobManager() {
        return jobManager;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
