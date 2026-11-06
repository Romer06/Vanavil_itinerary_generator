import { useState } from 'react';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";

export default function App() {
  const [formData, setFormData] = useState({ clientName: '', travelDates: '', rawItinerary: '' });

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const response = await fetch('https://vanavil-backend.onrender.com/api/itinerary/generate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      if (!response.ok) throw new Error('Failed to generate PDF');

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      
      // Using the fixed backticks for the dynamic file name!
      const cleanName = formData.clientName.replace(/\s+/g, '_');
      link.setAttribute('download', `${cleanName}_Vanavil_Itinerary.pdf`); 
      
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Error downloading PDF:", error);
    }
  };
  return (
    <div className="min-h-screen bg-background text-foreground flex items-center justify-center p-6 font-sans">
      {/* Removed the shadow-2xl and heavy borders */}
      <Card className="w-full max-w-2xl border-border">
      <CardHeader className="space-y-2 text-center pb-8 pt-8">
          
          <div className="flex justify-center w-full mb-4">
           <img 
  src="/vanavil-logo.png" 
  alt="Vanavil Tours & Holidays" 
  className="w-full max-w-[700px] object-contain mix-blend-mode-screen filter contrast-125 drop-shadow-[0_0_20px_rgba(0,210,255,0.15)]" 
/>
          </div>
          
          <CardDescription className="text-primary font-mono text-xs uppercase tracking-widest mt-2">
            White-Label Itinerary Generator
          </CardDescription>
          
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="clientName">Client Name</Label>
                {/* Let shadcn's default transparent input styling take over */}
                <Input 
                  id="clientName" 
                  placeholder="e.g. John Doe" 
                  value={formData.clientName}
                  onChange={(e) => setFormData({...formData, clientName: e.target.value})}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="travelDates">Travel Dates</Label>
                <Input 
                  id="travelDates" 
                  placeholder="5th Nov - 12th Dec" 
                  value={formData.travelDates}
                  onChange={(e) => setFormData({...formData, travelDates: e.target.value})}
                  required
                />
              </div>
            </div>
            <div className="space-y-2">
              <Label htmlFor="rawItinerary">Raw DMC Itinerary Text</Label>
              <Textarea 
                id="rawItinerary" 
                placeholder="Paste the daily schedule here..." 
                className="min-h-[250px] font-mono text-sm leading-relaxed resize-none"
                value={formData.rawItinerary}
                onChange={(e) => setFormData({...formData, rawItinerary: e.target.value})}
                required
              />
            </div>
           <Button 
  type="submit" 
  className="w-full mt-4 font-mono font-bold tracking-widest text-xs uppercase py-6 bg-primary hover:bg-primary/90 text-primary-foreground shadow-lg shadow-primary/20"
>
  Generate Branded PDF
</Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}

  