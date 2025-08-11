/* @refresh reload */
import { render } from 'solid-js/web'
import Routes from './Routes.tsx'
import "./index.scss"

const root = document.getElementById('root')

render(() => <Routes />, root!)
