package com.example.akash.proofofconcept;

import android.test.mock.MockContext;
import android.view.View;
import com.example.akash.proofofconcept.model.CountryFact;
import com.example.akash.proofofconcept.viewmodel.CountryItemViewModel;
import com.example.akash.proofofconcept.viewmodel.CountryViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.Silent.class) public class ItemViewModelTest {

    private static final String COUNTRY_FACT_HEADER_TEST = "Beavers";



    @Test public void shouldGetCountryFactHeader() throws Exception {
        CountryFact countryFact = new CountryFact();
        countryFact.title = COUNTRY_FACT_HEADER_TEST;
        CountryItemViewModel countryItemViewModel = new CountryItemViewModel(countryFact);
        assertEquals(countryFact.title, countryItemViewModel.getHeader());
    }
}