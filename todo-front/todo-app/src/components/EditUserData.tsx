import { useState } from "react";
import type { User } from "../types/types";
import { fetchWithAuth } from "../utils/fetchWithAuth";

type Props = {
    user: User;
    onCancel: () => void;
    onSave: (updated: User) => void;
};

const EditUserData = ({user, onCancel, onSave}: Props) => {
    const [username, setUsername] = useState(user.username || "");
    const [email, setEmail] = useState(user.email || "");
    const [password, setPassword] = useState("");
    const [age, setAge] = useState<number | undefined>(user.age);
    const [phoneNumber, setPhoneNumber] = useState(user.phone_number || "");

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const updatedFields: Partial<User> = {};

        if (username.trim()) updatedFields.username = username;
        if (email.trim()) updatedFields.email = email;
        if (password.trim()) updatedFields.password = password;
        if (typeof age === "number") updatedFields.age = age;
        if (phoneNumber.trim()) updatedFields.phone_number = phoneNumber;

        const res = await fetchWithAuth(
            "/users/update",
            {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(updatedFields)
            }
        );

        if (res.ok) {
            const updated = await res.json();
            onSave(updated);
        } else {
            const error = await res.json();
            alert(`❌ Error: ${error.message}`);
        }
    };

    const isModified =
        username !== user.username ||
        email !== user.email ||
        password !== "" ||
        age !== user.age ||
        phoneNumber !== user.phone_number;

    return (
        <form onSubmit={handleSubmit}>
            <input 
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Username"
            />
            <input 
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email"
            />
            <input 
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password"
            />
            <input 
            type="number"
            value={age ?? ""}
            onChange={(e) => {
                const value = e.target.value;
                setAge(value === "" ? undefined : Number(value));
            }}
            placeholder="Age"
            />
            <input 
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            placeholder="PhoneNumber"
            />
            <button type="submit" disabled={!isModified}>💾 Save</button>
            <button type="button" onClick={onCancel}>
            ❌ Cancel
            </button>
        </form>
    )
};

export default EditUserData;
