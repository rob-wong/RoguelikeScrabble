package testextension

import com.example.gymapprefactor.common.components.presentation.TopBarState
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutinesTestExtension : BeforeAllCallback, AfterAllCallback {

    @Singleton // see if collect works with this
    companion object {
        val testDispatcher: TestDispatcher = StandardTestDispatcher()
        val universalTopBar: TopBarState.Content = mockk()
    }

    override fun beforeAll(context: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}
