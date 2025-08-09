import { createSignal } from 'solid-js';
import { fetchUserState } from './api/api';
import { PageLogin } from './page/Login';




function App() {
  let [user, setUser] = createSignal<number|undefined>(undefined)

  
  return (<PageLogin onLogin={()=>{console.log("hello");
  }}></PageLogin>)
}

export default App
