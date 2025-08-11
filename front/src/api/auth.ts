
export async function fetchUserLogin (username: string, password: string) {
    return (await fetch(
        "/user/login",
        {
            method: "POST",
            body: JSON.stringify({
                username: username,
                password: password
            })
        }
    )).json() as Promise<{success: boolean}>
}

export async function fetchUserState() {
    return (await fetch("/user/state")).json() as Promise<{logged: false}|{logged: true, id?: number, name?: string}>
}

export async function fetchUserLogout() {
    await fetch("/user/logout")
}

