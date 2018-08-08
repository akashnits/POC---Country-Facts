package com.example.akash.proofofconcept;

import android.test.mock.MockContext;
import android.view.View;
import com.example.akash.proofofconcept.model.CountryFact;
import com.example.akash.proofofconcept.networkUtils.ApiService;
import com.example.akash.proofofconcept.viewmodel.CountryViewModel;
import io.reactivex.Observable;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.Silent.class) public class MainViewModelTest {



    @Mock private MockContext mockContext;

    private CountryViewModel countryViewModel;

    @Before public void setUpMainViewModelTest() {
        countryViewModel = new CountryViewModel(mockContext);
    }


    @Test public void ensureTheViewsAreInitializedCorrectly() throws Exception {
        countryViewModel.updateViews();
        assertEquals(View.GONE, countryViewModel.countryLabel.get());
        assertEquals(View.GONE, countryViewModel.countryRecyclerView.get());
    }
}