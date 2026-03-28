import "../styles/auth.css";

export default function AuthLayout({
                                       title,
                                       subtitle,
                                       children,
                                       sideTitle = "GlobalConnect",
                                       sideText = "Connect buyers and sellers in a modern auction marketplace.",
                                   }) {
    return (
        <div className="auth-page">
            <div className="auth-left">
                <div className="auth-brand-badge">GC</div>
                <h1>{sideTitle}</h1>
                <p>{sideText}</p>
            </div>

            <div className="auth-right">
                <div className="auth-card">
                    <h2>{title}</h2>
                    {subtitle && <p className="auth-subtitle">{subtitle}</p>}
                    {children}
                </div>
            </div>
        </div>
    );
}