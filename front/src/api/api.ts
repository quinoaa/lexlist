
const URL = "http://localhost:8080"

export const fetchUserState = async () => {
    return (await fetch(URL + "/user/state")).json() as Promise<{logged: boolean, id?: number, name?: string}>
}

export const fetchUserLogin = async (username: string, password: string) => {
    return (await fetch(
        URL + "/user/login",
        {
            method: "POST",
            body: JSON.stringify({
                username: username,
                password: password
            })
        }
    )).json() as Promise<{success: boolean}>
}

export const fetchUserLogout = async () => {
    await fetch(URL + "/user/logout")
}
